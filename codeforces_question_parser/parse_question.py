import sys
import time
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.support.wait import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

class ParseQuestions():
    def __init__(self, starting_contest, contest_type, difficulty_level, number_of_questions):
        url = "https://codeforces.com/contests"
        options = Options()
        options.headless = True 
        driver = webdriver.Chrome(ChromeDriverManager().install(), options=options)
        driver.set_window_rect(width=1200, height=900)

        # Get contests urls
        driver.get(url)
        self.driver = driver
        is_starting_contest_found = False
        self.page_count = 1
        self.arr = []
        self.__get_contests_urls(starting_contest, contest_type, number_of_questions, is_starting_contest_found)
        if len(self.arr) >= 0:
            print('\033[92m'+f"Test PASSED\nGot {len(self.arr)}/{number_of_questions} contests")
        else:
            print('\033[91m'+"Test FAILED\n Length of 'arr' is 0")
        self.arr = self.arr[:number_of_questions]
        time.sleep(3)
        
        # Get questions link from contests link
        self.questions_arr = []
        self.__get_problems_urls(difficulty_level)
        if len(self.questions_arr) > 0:
            print('\033[92m'+f"Test PASSED\nGot {len(self.questions_arr)}/{number_of_questions} problems")
        else:
            print('\033[91m'+"Test FAILED\n Length of questions_arr is", len(self.questions_arr))
            sys.exit(1)
        time.sleep(3)
        
        # print all the questions
        if len(self.questions_arr) != 0:
            self.__print_questions_with_urls(self.questions_arr)

    def __get_contests_urls(self, starting_contest, contest_type, number_of_questions, is_starting_contest_found):
        print(f"\nSearching page {self.page_count} for {contest_type} contests...")
        self.page_count += 1

        driver = self.driver
        arr = self.arr

        # waiting for page to load and get all the contests lists
        contests_table = WebDriverWait(driver, 10).until(EC.element_to_be_clickable((webdriver.common.by.By.CSS_SELECTOR, "div.contests-table table")))
        contests_list = contests_table.find_elements(webdriver.common.by.By.TAG_NAME, "tr")[1:]

        # finding next page button to click
        pagination = driver.find_element(webdriver.common.by.By.CSS_SELECTOR, "div.pagination")
        next_page_button = pagination.find_elements(webdriver.common.by.By.TAG_NAME, "a")[-1]
        if next_page_button.text != "→":
            next_page_button = None

        # process contest data and try finding if there is the starting contest in this page
        for contest in contests_list:
            contest_data = contest.find_element(webdriver.common.by.By.TAG_NAME, "td")
            contest_name = contest_data.text[:contest_data.text.index("\n")]
            contest_url = contest_data.find_element(webdriver.common.by.By.TAG_NAME, "a").get_attribute("href")
            if not is_starting_contest_found:
                if starting_contest in contest_name:
                    is_starting_contest_found = True
            if is_starting_contest_found and contest_type in contest_name:
                arr.append([contest_name, contest_url])
        
        # go to the next page if we are short of target
        if len(arr) < number_of_questions:
            if next_page_button:
                next_page_button.click()
                time.sleep(3)
                self.__get_contests_urls(starting_contest, contest_type, number_of_questions, is_starting_contest_found)
    

    def __get_problems_urls(self, difficulty_level):
        driver = self.driver
        for contest_details in self.arr[::-1]:
            # load the webpage
            driver.get(contest_details[1])

            # wait for the page to load
            all_contest_problems_table = WebDriverWait(driver, 10).until(EC.element_to_be_clickable((webdriver.common.by.By.CSS_SELECTOR, "table.problems")))

            # get question with given rating and store it
            problem_a_urls = all_contest_problems_table.find_elements(webdriver.common.by.By.TAG_NAME, "a")
            for problem in problem_a_urls:
                if problem.text == difficulty_level:
                    problem_a_url = problem.get_attribute('href')
                    self.questions_arr.append([contest_details[0], problem_a_url])



    
    def __print_questions_with_urls(self, questions_arr):
        for i in range(len(questions_arr)):
            print(f"{i+1}.\t[{questions_arr[i][0]}]({questions_arr[i][1]})")
