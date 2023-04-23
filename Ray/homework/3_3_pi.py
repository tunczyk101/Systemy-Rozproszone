# 3.3 Take a look on implement parallel Pi computation
# based on https://docs.ray.io/en/master/ray-core/examples/highly_parallel.html
#
# Implement calculating pi as a combination of actor (which keeps the
# state of the progress of calculating pi as it approaches its final value)
# and a task (which computes candidates for pi)

import ray
import random
import time
import math
from fractions import Fraction
import logging

if ray.is_initialized:
    ray.shutdown()
ray.init(logging_level=logging.ERROR)




@ray.remote
def pi4_sample(sample_count):
    """pi4_sample runs sample_count experiments, and returns the
    fraction of time it was inside the circle.
    """
    in_count = 0
    for i in range(sample_count):
        x = random.random()
        y = random.random()
        if x * x + y * y <= 1:
            in_count += 1
    return in_count, sample_count


@ray.remote
class Pi:
    def __init__(self):
        self.in_count = 0
        self.sample_count = 0

    def add_sample(self, in_count, sample_count):
        self.in_count += in_count
        self.sample_count += sample_count

    def calculate_pi(self):
        if self.sample_count != 0:
            return 4 * self.in_count / self.sample_count
        return 0

    def get_in_count(self):
        return self.in_count

    def get_sample_count(self):
        return self.sample_count


def compute_pi(batches, samples):
    print(f'Doing {batches} batches')
    pi_calculator = Pi.remote()
    print(pi_calculator)
    for i in range(batches):
        in_count, sample_count = ray.get(pi4_sample.remote(sample_count=samples))
        pi_calculator.add_sample.remote(in_count, sample_count)

        if (i + 1) % 10 == 0:
            print(f"Pi after {i + 1} batches: {ray.get(pi_calculator.calculate_pi.remote())}")

    results = pi_calculator.calculate_pi.remote()
    pi = ray.get(results)
    print(f"Pi: {float(pi)}")
    print(f"math pi: {math.pi}")
    print(f"difference: {abs(pi - math.pi) / pi}")


SAMPLE_COUNT = 1000000
BATCHES = 1000
compute_pi(BATCHES, SAMPLE_COUNT)

ray.shutdown()
