import os
import sys
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from homepage_parser import HomePageParser
from trains_list_page_parser import TrainsListPageParser


os.environ['PATH'] = "/Users/ashutoshkumar/seleniumdrivers"
options = Options()
options.headless = True 
driver = webdriver.Chrome(options=options)
driver.set_window_rect(width=1200, height=900)
url = "https://www.irctc.co.in/nget/booking/train-list"
driver.get(url)

# parse and automate homepage
source = sys.argv[1]
destination = sys.argv[2]
date = sys.argv[3]
homePageParser = HomePageParser(driver, source, destination, date)
isHomePageParsed = homePageParser.parseAndAutomate()

# parse next page
if isHomePageParsed:
    tlpp = TrainsListPageParser(driver)
    tlpp.parseData()