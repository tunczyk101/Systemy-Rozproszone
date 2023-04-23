import random
import logging
import cProfile
import ray


# Exercise 1.1)Try using local bubble sort and remote bubble sort,
# show difference


if ray.is_initialized:
    ray.shutdown()
ray.init(logging_level=logging.ERROR)


# ray.init(address='auto', ignore_reinit_error=True, logging_level=logging.ERROR)


def bubble_sort(arr):
    length = len(arr)

    for i in range(length):
        for j in range(0, length - i - 1):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]


@ray.remote
def ray_bubble_sort(arr):
    return bubble_sort(arr)


n = 500

array1 = [[random.randint(0, n) for _ in range(n)] for i in range(n)]


def run_local_bubble_sort(arrays):
    results = [bubble_sort(arr) for arr in arrays]
    return results


print('local run')
cProfile.run("run_local_bubble_sort(array1)")


def run_remote_bubble_sort(arrays):
    results = ray.get([ray_bubble_sort.remote(arr) for arr in arrays])
    return results


print('remote run')
cProfile.run("run_remote_bubble_sort(array1)")

ray.shutdown()
