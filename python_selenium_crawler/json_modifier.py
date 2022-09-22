import json

# markdown Div2's links
# data = None
# with open("Div2.json") as filename:
#     data = json.load(filename)

# with open("Div2.md", "w") as filename:
#     filename.write("# Contests link")
#     filename.write("\n")
#     count = 1
#     for url in data['urls']:
#         if "https" in url:
#             url = url.replace('\"','')
#             url = str(count) + "." + url
#             count += 1
#         filename.write("\n")
#         filename.write(url)


# markdown questions link
# data = None
# with open("Questions.json") as filename:
#     data = json.load(filename)

# with open("Questions.md", "w") as filename:
#     filename.write("# Codeforces questions")
#     filename.write("\n")
#     count = 1
#     for info in data:
#         code = info["problem_code"]
#         name = info["problem_name"]
#         url = info["problem_url"]
#         url = url.replace('\"','')
#         line = str(count) + "." + f"[{code}  {name}]({url})"
#         count += 1
#         filename.write("\n")
#         filename.write(line)