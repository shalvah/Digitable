# Digitable
An Android app for managing students' class and assignment schedule in order to enhance productivity.

Core Functions:
1. Assignment Tracker: Add assignments with deadline, priority, reminder etc. View list of assignments.
2. Lecture Tracker: Add lectures with courses, start and end times. View list of lectures.
3. Reader: Shows you relevant articles from the web on your field of study and other preferences you specified

Development Modules (How we broke down the dev process--not in any order):
1. Home
2. Courses
3. Timetable
4. Assignments
5. Reader
6. Others (Welcome, About)

Technology used in the Reader module:
*Queries formulated by randomizing a word from "queryCompanion" and combining with a preference
*Google Custom Search API used
*Wrote some custom code to parse JSON from gCSE results (GSON and others were larger than what was needed)

Other Features:
*Track student's assignments/deadlines and provide reminders.
*Show assignments with upcoming deadlines on Home
*Show complete assignment list, which can be sorted by priority or deadline (not implemeted yet)
*Track lectures and provide reminders
*Show current and upcoming lectures on Home
*Show complete lecture list


Feel free to contribute 
