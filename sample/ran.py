import random
total = 0
p = 75
a = []
b = []
for i in range(p):
    x = random.randint(10,65)
    y = random.randint(2,19)
    total += y
    a.append(x) 
    b.append(y)
print(total)
print(p, int(total/1.87))
for i in range(p):
    print(a[i],b[i])