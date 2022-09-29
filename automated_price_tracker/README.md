# Automated Price tracker 

In this project I have build an app, that would get the total availabile seats and ticket price of a train for a particular day, everyday.\
I have used `Python scipt`, `Selenium webdriver`, `mac automator app` and `mac calendar app` for this purpose.

# Steps
1. [Create a python script to scrape a website for information](#step-1)
2. [Use mac's automator app to build an application that would take input from the user, run the python script (created in step 1) based on this user input and display the details as a notification](#step-2)
3. [Integrate the app (created in step 2) with mac's calendar app to run it everyday](#step-3)

### Step 1

The code for this present [here](https://github.com/litoco/SmallProjects/tree/main/automated_price_tracker/train_list_parser)\
In this I have taken IRCTC site as an example to fetch train data for the particular date and between source and destination 
1. So fistly I get all the necessary input data (like source, destination and date of journey) required on the [homepage of irctc website](https://www.irctc.co.in/nget/train-search)
2. Then I put these data in the website and click on submit button through python selenium automator.
3. Then it takes us to the next page where the actual train data is available.
4. After that I parse this website to get the correct train data and store it in a json file like [this](https://github.com/litoco/SmallProjects/blob/main/automated_price_tracker/train_list_parser/trains_list.json).


### Step 2

Now that we have the code that gets the input from user, does the processing on it and outputs a json file holding train information.\
We now need an application that get the necessary information from the user and pass it to the python code and run the code to get the output stored in a json file.

For that we will use Mac Automator here. And following are the steps to be followed:
1. Open automator in mac
2. Choose create a new file by clicking on file (top left corner) > new > application > choose
3. Then we need to get the necessary inputs (like source station code, destination station code and date of journey) from the user, we will use `ask for text` action for this purpose. Just search for `ask for text` in the search box as shown below and double click it add it to the workflow.
We will add 3 of them each for (*source station*, *destination station* and *date of jouney*)

![image](https://i.stack.imgur.com/qw56U.png)

4. Now that we have got the input from the user we need to store it in a variable. So for that we will search for `set value of variable` action in the seach box (as explained in step 3) and put it below every `ask for text` action. So we will have 3 of these as well.\
We need to focus on 2 things here: \
first is that the variable should have different name. We can do that by clicking on the arrow button in the `set value of variable` and click on `new variable` and provide a suitable name to the varible.\
It should look something like below:

![image2](https://i.stack.imgur.com/75L1k.png)

second for each of these user inputs we need to see for `ask for text` connected with `set value of variable`. If this is not the case, right click on `set value of variable` box and click on `accept input` to connect it. It is important to note that `ask for text` connected with below `set value of variable` and not the vice-versa. If `set value of variable` is connected with below `ask for text` we need to remove this connection by right clicking on `ask for text` and clicking `ignore input` text. 

5. Now that we have assigned these three value to their corresponding variable. We now need to get the value of these variables to pass them to the python file. For that purpose we will search for `get value of variable` in the search box (as specified in step 3) and add it to the workflow.\
We will need 3 of these as well. After that we need to connect these 3 `get value of variable` actions one after the other in the same way as we connected the actions in the latter part of step 4.\
It should look something like below:


![image3](https://i.stack.imgur.com/MAoWh.png)

6. On successful completion of step 5 we will pass these user inputs to the python file. To do that seach for `run shell script` action and add it to the workflow. Connect this action with the `get value of variable` action present above by the same way as explained in the latter part of step 4. Change the `pass input` (present on the right side of the action `run shell script`) to `as arguments`.\
And to run the code put `/usr/local/bin/python3 python/train_list_parser/selenium_scrapper.py $1 $2 $3` in the input section of `run shell script`.\
This will run the python code to fetch train data from irctc based on user input and put it to `trains_list.json` file after successful parsing.\
It should look like the following:

![image4](https://i.stack.imgur.com/Z9QL9.png)

7. Finally we can open the `train_list.json` file to see the trains detail (optional step). For that we will search and include `run shell script` action and to open the file, we need to put `open /Users/username/path/to/file/train_list_parser/trains_list.json` code in the input box of the `run shell script` action.

8. Save this application.


### Step 3

Now that we have the application that asks for user input and outputs the train details to the `trains_list.json` file, we will integrate it to the calendar app on mac to run it periodically as we want.

For that:
1. Open calendar
2. Create a new event (File > New Event)
3. Provide an appropriate name to this event
4. Click on the event to open event pop up.
5. Click on the date present in the event pop up to edit it.
6. Provide the necessary details and click on the alert section
7. Scroll to the last and click on custom
8. From the first input box click on open file
9. Click on the second input box and provide the location of the application that we have created above in [step 2](#step-2)
10. Select the application and click select
11. click on okay (at the bottom end)
12. You have your periodically running application with you

Thats all that we needed to run an application automatically and periodically in you local mac.
