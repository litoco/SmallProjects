from parse_question import ParseQuestions
import sys

def print_error():
    print("Invalid input")
    sys.exit(0)
if __name__ == "__main__":
    starting_contest = input("Enter the contest name from where you want to start the search: ")
    starting_contest.strip()
    if len(starting_contest) == 0:
        print_error()
    
    contests = ["Global Round", "Div. 1", "Div. 2", "Div. 3", "Div. 4"]
    helper = "1. Global Rounds\n2. Div. 1\n3. Div. 2\n4. Div. 3\n5. Div. 4\n"
    contest_type = input(f"Which contests do you want to practice:\n{helper}")
    contest_type.strip()
    if contest_type.find(".") != -1:
        contest_type = contest_type[:-1]
    try:
        contest_type = int(contest_type)
        contest_type = contests[contest_type-1]
    except:
        print_error()
    
    difficulties = ["A", "B", "C", "D", "E"]
    helper = "1. A\n2. B\n3. C\n4. D\n5. E\n"
    difficulty_level = input(f"Enter the difficulty level:\n{helper}")
    difficulty_level.strip()
    if difficulty_level.find(".") != -1:
        difficulty_level = difficulty_level[:-1]
    try:
        difficulty_level = int(difficulty_level)
        difficulty_level = difficulties[difficulty_level-1]
    except:
        print_error()
    
    num_of_questions = input("How many questions do you want?\n")
    try:
        num_of_questions = int(num_of_questions)
    except:
        print_error()
    
    ParseQuestions(starting_contest, contest_type, difficulty_level, num_of_questions)