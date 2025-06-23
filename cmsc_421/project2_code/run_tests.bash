#!/bin/bash

###############################################################################
# run_tests.bash -- Dana Nau <nau@cs.umd.edu>, Sept 10, 2018
#
# This bash script uses three lists -- racetrack problems, heuristic functions,
# and search strategies -- and iterates over all possible combinations. For each
# combination, it does (1) a graphic demo of the search and (2) a time test.
#
# You should modify this file to use the sample problems, heuristics, and
# search strategies that you want to use.
###############################################################################


set -f    # disable globbing, because we don't want the name a* to be expanded

###############################################################################
### START OF CUSTOMIZATION OPTIONS


### IMPORTANT!!! Change this to use the pathname for your python program
#
python=C:/Users/Willi/AppData/Local/Programs/Python/Python37-32/python.exe    # python pathname


#### TEST PROBLEMS ###

# Specify the rootname of the file that contains the test problems

prob_file=sample_probs

# Below, the first line is a list of all the problems in sample_probs.py,
# in roughly increasing  order of difficulty. You should modify the second
# line to include just the one(s) you want to use.

# problems=(rect40a rect40b rect40c rect40d rect40e rect100 wall16a wall16b lhook32 rhook32a rhook32b spiral32 spiral48 pdes60 pdes60b rectwall16 rectwall32 rectwall64 rectwall64a)
#  rectwall48 rectwall96 rectwall96a
problems=(rect20a rect20b rect20c rect20d rect50 wall8a wall8b lhook16 rhook16a rhook16b spiral16 spiral24 pdes30 pdes30b rectwall8 rectwall16 rectwall32 rectwall32a)

# Use draw=1 to draw the search tree; use draw=0 to draw nothing
draw=1


### End of customization options
#####################################################################


# The following code will iterate over every combination of sample problem,
# heuristic function, and search strategy in your above lists. For each combination,
# it will first display the results graphically, and then do a time test.
#
for prob in ${problems[*]}
do
  # strings for setup
  setup="import supervisor, $prob_file"

  # execute the setup in python
  $python -c "$setup"

  # Here's the code for doing a time test.
  echo ''
  echo "Time test of '$prob'"

  # string to do a time test (without printing or drawing anything)
  time_test="supervisor.main($prob_file.$prob,draw=$draw)"

  # execute the time test
  $python -m timeit -n 1 -r 1 -s "$setup" "$time_test"
done
