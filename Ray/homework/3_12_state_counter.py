# exercise 3
# 3.0 start remote cluster settings and observe actors in cluster
# a) make screenshot of dependencies
# 3.1. Modify the Actor class MethodStateCounter and add/modify methods that return the following:
# a) - Get number of times an invoker name was called
# b) - Get a list of values computed by invoker name
# 3- Get state of all invokers
# 3.2 Modify method invoke to return a random int value between [5, 25]
import random
import time

import ray
import logging

if ray.is_initialized:
    ray.shutdown()
ray.init(logging_level=logging.ERROR)

# ray.init(address='auto', ignore_reinit_error=True, logging_level=logging.ERROR)

CALLERS = ["A", "B", "C"]


@ray.remote
class MethodStateCounter:
    def __init__(self):
        self.invokers_count = {i: 0 for i in CALLERS}
        self.invokers_values = {i: [] for i in CALLERS}

    def invoke(self, caller_name):
        # pretend to do some work here
        time.sleep(0.5)
        # update times invoked
        self.invokers_count[caller_name] += 1
        val = random.randint(5, 25)
        self.invokers_values[caller_name].append(val)
        # return the state of that invoker
        return val

    def get_invoker_count(self, caller_name):
        # return the state of the named invoker
        return self.invokers_count[caller_name]

    def get_invoker_values(self, caller_name):
        return self.invokers_values[caller_name]

    def get_all_invoker_counts(self):
        # return the state of all invokers
        return self.invokers_count

    def get_all_invokers_values(self):
        return self.invokers_values


worker_invoker = MethodStateCounter.remote()
print(worker_invoker)


for _ in range(10):
    name = random.choice(CALLERS)
    worker_invoker.invoke.remote(name)

print('method callers')
for _ in range(5):
    random_name_invoker = random.choice(CALLERS)
    value = ray.get(worker_invoker.invoke.remote(random_name_invoker))
    print(f"Named caller: {random_name_invoker} called {value}")

# Fetch the count of all callers
print(ray.get(worker_invoker.get_all_invokers_values.remote()))
