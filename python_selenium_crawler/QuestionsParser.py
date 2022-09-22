import os
import re
import time
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait

class QuestionsParser():
    def __init__(self, url_list):
        self.url_list = url_list
    
    def parse_questions(self, number_of_questions_per_difficulty_level):
        url_list = self.url_list
        all_questions_lists = []
        all_questions_urls_list = []
        os.environ['PATH'] = "/Users/ashutoshkumar/seleniumdrivers"
        options = Options()
        options.headless = True
        driver = webdriver.Chrome(options=options)
        for url in url_list:
            print("Getting questions from", url)
            driver.get(url)
            d = {}
            self.__parse_questions_for_url(driver, d)
            questions_list = self.__get_questions_list(d, number_of_questions_per_difficulty_level, all_questions_urls_list)
            all_questions_lists.extend(questions_list)
            time.sleep(3)
        driver.close()
        return all_questions_lists

    
    def __parse_questions_for_url(self, driver, d):
        problems_table = WebDriverWait(driver, 10).until(EC.element_to_be_clickable((webdriver.common.by.By.CSS_SELECTOR, "table.problems")))
        for row in problems_table.find_elements(webdriver.common.by.By.TAG_NAME, "tr"):
            try: # parse questions details
                problem_details = row.find_element(webdriver.common.by.By.TAG_NAME, "td")
                problem_url = problem_details.find_element(webdriver.common.by.By.TAG_NAME, "a").get_attribute("href")
                problem_name_and_tag = row.find_element(webdriver.common.by.By.CSS_SELECTOR, "td:nth-of-type(2)").text
                posn = problem_name_and_tag.index("\n")
                problem_name = problem_name_and_tag[:posn]
                problem_tag = problem_name_and_tag[posn+1:]
                tmp_d = {}
                tmp_d["problem_code"] = problem_details.text
                tmp_d["problem_url"] = problem_url
                tmp_d["problem_name"] = problem_name
                tmp_d["problem_tag"] = problem_tag
                match_obj = re.search("[A-Z]+",problem_details.text)
                code = match_obj.group()
                if d.get(code, -1) == -1:
                    d[code] = [tmp_d]
                else:
                    d[code].append(tmp_d)
            except Exception as e:
                pass
        try: 
            pagination = WebDriverWait(driver, 10).until(EC.element_to_be_clickable((webdriver.common.by.By.CSS_SELECTOR, "div.pagination")))
            last_anchor_tag = pagination.find_elements(webdriver.common.by.By.TAG_NAME, "a")[-1]
            if last_anchor_tag.text == "→":
                last_anchor_tag.click()
                time.sleep(3)
                print("moving to next page...")
                self.__parse_questions_for_url(driver, d)
        except Exception as ex:
            pass

    def __get_questions_list(self, question_dict, num_of_questions_per_difficulty, all_questions_urls_list):
        all_questions_arr = []
        for difficulty in question_dict:
            questions_arr = []
            for question in question_dict[difficulty]:
                question_tags = question['problem_tag']
                question_url = question['problem_url']
                match_obj = re.search('\*+', question_tags)
                if not match_obj and question_url not in all_questions_urls_list:
                    questions_arr.append(question)
                    all_questions_urls_list.append(question_url) # to remove duplicates
                    if len(questions_arr) >= 10:
                        break
            all_questions_arr.extend(questions_arr)
        return all_questions_arr
                    
                
            


