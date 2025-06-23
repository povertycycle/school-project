import proj2
import sys
from problem import *
problem =  [(5,12), [(15,5),(15,15)], [[(0,0),(20,0)], [(20,0),(20,20)], [(20,20),(0,20)], [(0,20),(0,0)]]]

def main():
    state = (problem[0], (0, 0))
    walls = problem[2]
    finish = problem[1]
    proj2.initialize(state, finish, walls)
    proj2.main(state, finish, walls)
    sys.stdout.write("Done.")

if __name__ == "__main__":
    main()
