i_to_j = {}
total = {}

def updateTable(table, i, l):
    for item in table:
        if item <= i:
            table[item] = 0
        if l.index(item) < l.index(i):
            table[item] = 0
    return table

def getMax(table):
    m = 0
    current = 0
    for item in table:
        if table[item] > m:
            m = table[item]
            current = item
        elif table[item] == m:
            if current > item:
                m = table[item]
                current = item
    return m, current

def calculate(total, l):
    first, firstpos = getMax(total)
    lst = []
    lst.append(firstpos)
    possible_next = first
    current = firstpos
    del total[firstpos]
    while possible_next != 0:
        total = updateTable(total, current, l)
        n, pos = getMax(total)
        possible_next = n
        current = pos
        if current != 0:
            lst.append(pos)
        else:
            lst.append(l[len(l) - 1])
            break
    return lst


l = [6,4,7,9,1,2,3,0,5,8,21,15,11,81,77]

for i in l:
    for j in l:
        if i <= j and l.index(i) <= l.index(j):
            i_to_j[(i, j)] = True
        else:
            i_to_j[(i, j)] = False

for i in l:
    total[i] = 0

for item in i_to_j:
    i, j = item
    if i_to_j[(i, j)] == True:
        total[i] = total[i] + 1
        
for item in total:
    total[item] = total[item] - 1

print(calculate(total, l))
