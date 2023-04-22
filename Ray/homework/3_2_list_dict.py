import random
import logging
import cProfile
import ray

# Exercise 2.1) Create large lists and python dictionaries,
# put them in object store. Write a Ray task to process them.


if ray.is_initialized:
    ray.shutdown()
ray.init(logging_level=logging.ERROR)


# ray.init(address='auto', ignore_reinit_error=True, logging_level=logging.ERROR)


@ray.remote
def process_list(some_list: list):
    result = [i ** 2 for i in some_list]
    # print(result)
    return result


@ray.remote
def process_dict(some_dict: dict):
    result = {el: key for key, el in some_dict.items()}
    # print(result)
    return result


@ray.remote
def process_list_and_dict(some_dict: dict, some_list: list):
    result = {list(some_dict.values())[i]: some_list[i] for i in range(len(some_list))}
    return result


max_element = 10000
n = 1000000
huge_list = [random.randint(0, max_element) for _ in range(n)]
# print(f"huge list: {huge_list}")
huge_list_ref = ray.put(huge_list)
print(f"huge_list_ref: {huge_list_ref}")

huge_dict = {i: random.randint(-max_element, 0) for i in range(n)}
# print(f"huge dict: {huge_dict}")
huge_dict_ref = ray.put(huge_dict)
print(f"huge_dict_ref: {huge_dict_ref}")

print("huge list processing")
# cProfile.run("print(ray.get(process_list.remote(huge_list_ref)))")
cProfile.run("process_list.remote(huge_list_ref)")

print("huge dictionary processing")
# cProfile.run("print(ray.get(process_dict.remote(huge_dict_ref)))")
cProfile.run("process_dict.remote(huge_dict_ref)")

print("huge list and dictionary processing")
# cProfile.run("print(ray.get(process_list_and_dict.remote(huge_dict_ref, huge_list_ref)))")
cProfile.run("process_list_and_dict.remote(huge_dict_ref, huge_list_ref)")
