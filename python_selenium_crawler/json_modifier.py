import json
data = None
with open("Questions.json") as filename:
    data = filename.readlines()

with open("Questions.json", "w") as filename:
    for line in data:
        if "https" in line:
            colon_place = line.index(":")
            line = line[:colon_place] + line[colon_place:].replace('\"','')
        filename.write(line)