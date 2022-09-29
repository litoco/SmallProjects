import time
import json 
from selenium import webdriver
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait

class TrainsListPageParser():

    def __init__(self, driver):
        self.driver = driver

    def parseData(self):
        driver = self.driver
        try:
            WebDriverWait(driver, 10).until(EC.element_to_be_clickable((webdriver.common.by.By.CLASS_NAME, "pre-avl")))
            d = {}
            d['dataList'] = []
            for trainDetails in driver.find_elements(webdriver.common.by.By.CLASS_NAME, "form-group.no-pad.col-xs-12.bull-back.border-all"):
                
                trainName = trainDetails.find_element(webdriver.common.by.By.CLASS_NAME, "col-sm-5.col-xs-11.train-heading")
                startTime = trainDetails.find_element(webdriver.common.by.By.CLASS_NAME, "col-xs-5.hidden-xs")
                endTime = trainDetails.find_element(webdriver.common.by.By.CSS_SELECTOR, "span.pull-right")
                travelTime = trainDetails.find_element(webdriver.common.by.By.CLASS_NAME, "col-xs-3.pull-left.line-hr")
                trainName.location_once_scrolled_into_view
                WebDriverWait(trainDetails, 30).until(EC.element_to_be_clickable((webdriver.common.by.By.CLASS_NAME, "pre-avl"))).click()
                time.sleep(3)
                listOfCompartments = WebDriverWait(trainDetails, 30).until(EC.presence_of_all_elements_located((webdriver.common.by.By.CSS_SELECTOR, "div.ui-menuitem-text strong span.hidden-xs")))
                
                dataDict = {}
                dataDict['trainName'] = trainName.text
                dataDict['startTime'] = startTime.text
                dataDict['endTime'] = endTime.text
                dataDict['travelTime'] = travelTime.text
                dataDict['availability'] = []

                for trainCompartmentType in listOfCompartments:
                    tmpDict = {}
                    cssSelector = webdriver.common.by.By.CSS_SELECTOR
                    availabilityTagCSS = "table tr td.link.ng-star-inserted div.pre-avl div:nth-of-type(2)"
                    priceTagCSS = "span.ng-star-inserted strong"
                    WebDriverWait(listOfCompartments, 30).until(EC.element_to_be_clickable(trainCompartmentType)).click()
                    time.sleep(3)
                    WebDriverWait(listOfCompartments, 30).until(EC.element_to_be_clickable(trainCompartmentType))
                    tmpDict['compartment'] = trainCompartmentType.text
                    availabilityTag = WebDriverWait(trainDetails, 30).until(EC.presence_of_element_located((cssSelector, availabilityTagCSS)))
                    tmpDict['availability'] = availabilityTag.text
                    priceTag = WebDriverWait(trainDetails, 30).until(EC.presence_of_element_located((cssSelector, priceTagCSS)))
                    tmpDict['price'] = priceTag.text
                    dataDict['availability'].append(tmpDict)

                d['dataList'].append(dataDict)
            
            with open("trains_list.json", 'w') as trainDetailsFile:
                jsonString = json.dump(d, trainDetailsFile)

            driver.quit()
            return True
        except Exception as e:
            driver.quit()
            print(e.with_traceback())
            return False