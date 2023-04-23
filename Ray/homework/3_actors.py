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

    def invoke(self, name):
        # pretend to do some work here
        time.sleep(0.5)
        # update times invoked
        self.invokers_count[name] += 1
        value = random.randint(5, 25)
        self.invokers_values[name].append(value)
        # return the state of that invoker
        return value

    def get_invoker_count(self, name):
        # return the state of the named invoker
        return self.invokers_count[name]

    def get_invoker_values(self, name):
        return self.invokers_values[name]

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

# 3.3 Take a look on implement parallel Pi computation
# based on https://docs.ray.io/en/master/ray-core/examples/highly_parallel.html
#
# Implement calculating pi as a combination of actor (which keeps the
# state of the progress of calculating pi as it approaches its final value)
# and a task (which computes candidates for pi)
