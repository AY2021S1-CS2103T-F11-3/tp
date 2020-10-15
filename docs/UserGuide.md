---
layout: page
title: ProductiveNUS
---

ProductiveNUS is a **desktop application for managing and scheduling your academic tasks, optimized for use via a Command Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI).
It is a convenient platform for you to keep track of your lessons and assignments at hand and being able to type quickly will make the process fast as well.

### Table of Contents
1. Quick Start
2. Features
3. FAQ
4. Summary of commands supported

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `11` or above installed in your Computer.

1. Download the latest `productivenus.jar` from [here](https://github.com/AY2021S1-CS2103T-F11-3/tp/releases).

1. Copy the file to the folder you want to use as the _home folder_ for ProductiveNUS.

1. Double-click the file to start the app. The GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>

   Some example commands you can try:

   * **`add`**`n/Lab report 3 d/23-04-2020 1230 mod/CS2100` : Adds an assignment named `Lab report 3` to your schedule.

   * **`delete`**`3` : Deletes the 3rd assignment shown in the current list.

   * **`import YOUR_NUSMODS_URL`** : Imports your timetable.

   * **`list`**`2` : Lists all lessons and assignments within 2 weeks (including this week).

   * **`remind`**`3` : Tags your assignment to receive reminders which will be displayed in `Your Reminders` section.

   * **`exit`** : Exits the application.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by you.<br>
  e.g. in `add n/NAME_OF_ASSIGNMENT`, `NAME_OF_ASSIGNMENT` is a parameter which can be used as `add n/Assignment 2`.

* Items in square brackets are optional.<br>
  e.g `n/NAME_OF_ASSIGNMENT [mod/MODULE_CODE]` can be used as `n/Assignment 2 mod/CS2100` or as `n/Assignment 2`.


* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME_OF_ASSIGNMENT d/DEADLINE`, `d/DEADLINE n/NAME_OF_ASSIGNMENT` is also acceptable.

</div>

### Adding a person: `add`

Adds an assignment into your schedule.

Format: `add n/NAME_OF_ASSIGNMENT d/DEADLINE_OF_ASSIGNMENT TIME_ASSIGNMENT_IS_DUE mod/MODULE​ [remind]`

**Tip:**
You can include the `remind` tag when adding the assignment instead of using the `remind` command after adding the assignment.

Examples:
* `add n/Lab report 3 d/23-04-2020 1230 mod/CS2100`
* `add n/Tutorial 2 d/29-06-2020 1400 mod/CS2100 remind`

### Deleting an assignment : `delete`

Deletes the specified assignment from the assignment list.

Format: `delete INDEX`

* Deletes the assignment at the specified `INDEX`.
* The index refers to the index number shown in the displayed assignment list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd assignment in the assignment list.

### Importing your timetable : `import`

Imports your NUSMods timetable data.

Format: `import YOUR_NUSMODS_URL`

* Imports lesson data based on your NUSMods timetable data.
* NUSMods timetable URL used is obtained by clicking on the "Share/Sync" timetable icon at NUSMods.

Examples:
* `import https://nusmods.com/timetable/sem-2/share?ES2660=SEC:G01`.

### Listing lessons and assignments : `list`

Format: `list [NUMBER]`

- Shows a list of lessons and assignments in your schedule within next `NUMBER` 
weeks, starting from the current date.
- `list` command without `NUMBER` displays your entire list of lessons and assignments 
stored in ProductiveNUS.


Examples: 
- `list 2` displays your lessons and assignments within the next
2 weeks (starting from the current date).
- `list 3` displays all your lessons and assignments within the next
3 weeks (starting from the current date).
- `list ` displays all your lessons and assignments.

### Setting reminders for assignments : `remind`
Tags the specified assignment to receive reminders which will be displayed in `Your Reminders` section.

Format: `remind INDEX`

* Tags the assignment at the specified `INDEX` to receive reminders.
* The index refers to the index number shown in the displayed assignment list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `remind 2` sets reminders for the 2nd assignment in the assignment list.

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

ProductiveNUS data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous ProductiveNUS app home folder.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**add** | `add n/NAME_OF_ASSIGNMENT d/DEADLINE mod/MODULE_CODE` <br> e.g., `add n/Math tutorial d/21/03/2020 11:00 AM mod/ST2334`
**delete** | `delete INDEX`<br> e.g., `delete 3`
**import** | `import NUSMODS_URL`
**list** | `list [NUMBER]` e.g., `list 2`, `list`
**remind** | `remind INDEX`
