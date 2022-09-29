import time
from selenium import webdriver
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.wait import WebDriverWait

class HomePageParser():
    def __init__(self, driver, source, destination, date):
        self.driver = driver
        self.source = source
        self.destination = destination
        self.date = date
        
    def parseAndAutomate(self):
        isCloseBannerSuccessful = self.__closeBanners()
        isInputBoxFillingSuccessful = self.__fillInputBoxes()
        isSubmitSuccessful = False
        if isInputBoxFillingSuccessful:
            isSubmitSuccessful = self.__submit()
        
        if isCloseBannerSuccessful and isInputBoxFillingSuccessful and isSubmitSuccessful:
            print("Homepage parsed and automated successfully")
            return True
        else:
            print("Some error occurred while working on homepage")
            return False

    def __closeBanners(self):
        driver = self.driver
        try:
            # close popup message
            WebDriverWait(driver, 10).until(EC.visibility_of_any_elements_located((webdriver.common.by.By.CLASS_NAME, "btn")))
            okayButton = driver.find_elements(webdriver.common.by.By.CLASS_NAME, "btn")[0]
            okayButton.click()
            time.sleep(2)
            # close side banner
            WebDriverWait(driver, 10).until(EC.visibility_of_any_elements_located((webdriver.common.by.By.ID, "disha-banner-close")))
            sideBanner = driver.find_element(webdriver.common.by.By.ID, "disha-banner-close")
            sideBanner.click()
            time.sleep(2)
            # close login banner
            # WebDriverWait(driver, 5).until(EC.visibility_of_any_elements_located((webdriver.common.by.By.CLASS_NAME, "fa.fa-window-close.pull-right.loginCloseBtn.ng-tns-c19-17")))
            # loginBanner = driver.find_element(webdriver.common.by.By.CLASS_NAME, "fa.fa-window-close.pull-right.loginCloseBtn.ng-tns-c19-17")
            # loginBanner.click()
            print("All banners closed")
            return True
        except Exception as e:
            print("Problem occurred while closing banners")
            return False
    
    def __fillInputBoxes(self):
        driver = self.driver
        try:
            # find input elements from the page
            fromInput = None
            dateInput = None
            toInput = None
            for inputElement in driver.find_elements(webdriver.common.by.By.TAG_NAME, "input"):
                if inputElement.get_attribute("class") == "ng-tns-c57-8 ui-inputtext ui-widget ui-state-default ui-corner-all ui-autocomplete-input ng-star-inserted":
                    fromInput = inputElement
                elif inputElement.get_attribute("class") == "ng-tns-c58-10 ui-inputtext ui-widget ui-state-default ui-corner-all ng-star-inserted":
                    dateInput = inputElement
                elif inputElement.get_attribute("class") == "ng-tns-c57-9 ui-inputtext ui-widget ui-state-default ui-corner-all ui-autocomplete-input ng-star-inserted":
                    toInput = inputElement

            # fill station data of journey starting station
            fromInput.send_keys(self.source)
            for listItem in driver.find_elements(webdriver.common.by.By.CLASS_NAME, "ng-star-inserted"):
                try:
                    if len(listItem.text) > 0:
                        garbageList = listItem.text.split("\n")
                        for text in garbageList:
                            if "PNBE" in text:
                                fromInput.clear()
                                fromInput.send_keys(text)
                                listItem.click()
                                break
                        break
                except:
                    pass
            time.sleep(2)

            # fill station data of journey destination station
            toInput.send_keys(self.destination)
            for listItem in driver.find_elements(webdriver.common.by.By.CLASS_NAME, "ng-star-inserted"):
                try:
                    if len(listItem.text) > 0:
                        garbageList = listItem.text.split("\n")
                        for text in garbageList:
                            if "NDLS" in text:
                                toInput.clear()
                                toInput.send_keys(text)
                                listItem.click()
                                break
                        break
                except:
                    pass
            time.sleep(2)

            # fill date of journey
            dateInput.send_keys("29/29")
            dateInput.clear()
            dateInput.send_keys(self.date)
            time.sleep(2)

            print("All input boxes filled")
            return True
        except Exception as e:
            print("Problem occurred while feeding data to input elements")
            return False
    
    def __submit(self):
        driver = self.driver
        try:
            # click the send button
            sendButton = None
            buttons_list = driver.find_elements(webdriver.common.by.By.TAG_NAME, "button")
            for button in buttons_list:
                if button.text == "Search":
                    sendButton = button
                    break
            
            WebDriverWait(driver, 10).until(EC.element_to_be_clickable((sendButton)))
            sendButton.click()
            time.sleep(3)
            
            print("Data submitted")
            return True
        except Exception as e:
            print(e.values)
            print("Problem occurred while submitting data")
            return False


if __name__ == "__main__":
    print("Go to /Users/ashutoshkumar/python directory and then run \"python3 selenium_scrapper.py\" ")