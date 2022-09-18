import json
from QuestionsParser import QuestionsParser

math_800_url = "https://codeforces.com/problemset?order=BY_SOLVED_ASC&tags=math%2C800-800"
number_theory_800_url = "https://codeforces.com/problemset?order=BY_SOLVED_ASC&tags=number+theory%2C800-800"
implementation_800_url = "https://codeforces.com/problemset?order=BY_SOLVED_ASC&tags=implementation%2C800-800"
constructive_algo_800_url = "https://codeforces.com/problemset?order=BY_SOLVED_ASC&tags=constructive+algorithms%2C800-800"
greedy_800_url = "https://codeforces.com/problemset?order=BY_SOLVED_ASC&tags=greedy%2C800-800"

urls_list = [math_800_url, number_theory_800_url, implementation_800_url, constructive_algo_800_url, greedy_800_url]

questions_parser = QuestionsParser(urls_list)
all_questions_list = questions_parser.parse_questions(10)
with open("Questions.json","w") as questions_file:
    json.dump(all_questions_list, questions_file, indent=4)