import json
data = None
with open("Div2.json") as filename:
    data = filename.readlines()

with open("Div2.txt", "w") as filename:
    for line in data:
        if "https" in line:
            line = line.replace('\"','')
        filename.write(line)