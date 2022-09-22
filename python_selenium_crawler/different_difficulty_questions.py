import os
import time
import json
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait

class ParseQuestion():
    
    def get_contests_list(self, difficulty_level, urls, driver, num_of_contest=100):
        # wait for page to load
        table = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((webdriver.common.by.By.CSS_SELECTOR, "div.contests-table table")))
        # find next page button to go to next page
        pagination = driver.find_element(webdriver.common.by.By.CSS_SELECTOR, "div.pagination")
        # parse contests url store them
        next_page_symbol = pagination.find_elements(webdriver.common.by.By.TAG_NAME, "a")[-1]
        rows = table.find_elements(webdriver.common.by.By.TAG_NAME, "tr")
        print("Getting questions...")
        for row in rows[1:]:
            td = row.find_element(webdriver.common.by.By.TAG_NAME, "td")
            current_contest_name = td.text[:td.text.index("\n")]
            if difficulty_level in current_contest_name:
                contest_link = td.find_element(webdriver.common.by.By.TAG_NAME, "a")
                urls.append(contest_link.get_attribute('href'))
                if len(urls) == num_of_contest:
                    difficulty_level = difficulty_level[:3] + difficulty_level[-1] 
                    file_name = difficulty_level + ".json"
                    d = dict()
                    d['urls'] = urls
                    with open(('%s') % (file_name), "w") as file_write:
                        json.dump(d, file_write, indent=4)
                        print(f"url of latest {num_of_contest} {difficulty_level} contest added to file {file_name}")
                    return urls
        # go to next page by clicking on the next page button
        print("Going to next page...")
        next_page_symbol.click()
        time.sleep(3)
        return self.get_contests_list(difficulty_level, urls, driver, num_of_contest)

    @staticmethod
    def helper_text():
        print("Enter contest division for which you want to practice:")
        print("1. Div. 1")
        print("2. Div. 2")
        print("3. Div. 3")
        print("4. Div. 4")

def main():
    try:
        ParseQuestion.helper_text()
        # Get all inputs
        print("Enter your choice number: ", end="")
        difficulty_level = int(input())
        parse_question = ParseQuestion()
        contests_type_list = ["Div. 1", "Div. 2", "Div. 3", "Div. 4"]
        num_of_contest = int(input(f"How many recent {contests_type_list[difficulty_level-1]} contests of you want?\n"))
        # start web driver in headless mode
        url = "https://codeforces.com/contests"
        os.environ['PATH'] = "/Users/ashutoshkumar/seleniumdrivers"
        options = Options()
        options.headless = True
        driver = webdriver.Chrome(options=options)
        driver.get(url)
        print("Going to url =",url)
        # parse questions
        parse_question.get_contests_list(contests_type_list[difficulty_level-1], [], driver, num_of_contest)
        # close driver
        driver.quit()
    except ValueError as te:
        print("You need to enter only numbers", end=" ")
        time.sleep(2)
        print("between 1 to 4 both inclusive")
        time.sleep(2)
        main()
    except Exception as e:
        # print("Error message", e.values) # print full error message
        print("Some error occurred", e.with_traceback())

if __name__ == "__main__":
    main()

    

