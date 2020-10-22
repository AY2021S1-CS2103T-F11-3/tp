---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<img src="images/ArchitectureDiagram.png" width="450" />

The ***Architecture Diagram*** given above explains the high-level design of the App. Given below is a quick overview of each component.

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.

</div>

**`Main`** has two classes called [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

Each of the four components,

* defines its *API* in an `interface` with the same name as the Component.
* exposes its functionality using a concrete `{Component Name}Manager` class (which implements the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component (see the class diagram given below) defines its API in the `Logic.java` interface and exposes its functionality using the `LogicManager.java` class which implements the `Logic` interface.

![Class Diagram of the Logic Component](images/LogicClassDiagram.png)

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

The sections below give more details of each component.

### UI component

![Structure of the UI Component](images/UiClassDiagram.png)

**API** :
[`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class.

The `UI` component uses JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* Executes user commands using the `Logic` component.
* Listens for changes to `Model` data so that the UI can be updated with the modified data.

### Logic component

![Structure of the Logic Component](images/LogicClassDiagram.png)

**API** :
[`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

1. `Logic` uses the `AddressBookParser` class to parse the user command.
1. This results in a `Command` object which is executed by the `LogicManager`.
1. The command execution can affect the `Model` (e.g. adding a person).
1. The result of the command execution is encapsulated as a `CommandResult` object which is passed back to the `Ui`.
1. In addition, the `CommandResult` object can also instruct the `Ui` to perform certain actions, such as displaying help to the user.

Given below is the Sequence Diagram for interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

### Model component

![Structure of the Model Component](images/ModelClassDiagram.png)

**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

The `Model`,

* stores a `UserPref` object that represents the user’s preferences.
* stores the address book data.
* exposes an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* does not depend on any of the other three components.


<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique `Tag`, instead of each `Person` needing their own `Tag` object.<br>
![BetterModelClassDiagram](images/BetterModelClassDiagram.png)

</div>


### Storage component

![Structure of the Storage Component](images/StorageClassDiagram.png)

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

The `Storage` component,
* can save `UserPref` objects in json format and read it back.
* can save the address book data in json format and read it back.

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Implemented\] Import timetable feature
The user can import information about their lessons by giving their NUSMods timetable URL to ProductiveNUS to import.

#### Reasons for implementation:
Users may find it inconvenient to constantly refer to their NUSMods timetable whenever they want to check if they are
available on a specific date and time. By giving users the option to add their lesson information into ProductiveNUS,
it will help increase the user's convenience as all their academic related schedule can be found in one place.

ProdutiveNUS can also better schedule the user's work with their timetable information available, avoiding any clashes
in schedule.

#### Current implementation:
- The import command is a typical command used in ProductiveNUS. It extends `Command` and overrides the method `execute`
in `CommandResult`. `ImportCommandParser` implements `Parser<ImportCommand>` and it parses the user's input to return an
`ImportCommand` object. The constructor of `ImportCommand` takes in the prefix url/ and the user's NUSMods timetable
url.

- A call to `TimetableRetriever` will be made. `TimetableRetriever` takes the user's timetable data which was parsed by
`ImportCommandParser` and makes a HTTP GET request to NUSMods API. NUSMods sends `TimetableRetriever` the relevant JSON
data. The data is parsed and returns as a list of `Lessons`.

The following sequence diagram shows the sequence when LogicManager executes `import` command.
![Interactions Inside the Logic Component for the `import url/URL` Command](images/FindSequenceDiagram.png)

1. The `execute` method of `LogicManager` is called when a user keys in an input into the application and `execute`
takes in the input.
2. The `parseCommand` method of `ProductiveNusParser` parses the user input and returns an initialized
`ImportCommandParser` object and further calls the `parse` method of this object to identify the URL in the user input.
3. It calls the `TimetableUrlParser` with the URL and it returns a `TimetableData` object.
4. `ImportCommandParser` returns an `ImportCommand` object.
5. There is return call to `LogicManager` which then calls the overridden `execute` method of `ImportCommand`.
6. The `execute` method of `Importommand` will call the `retrieveLessons` method from `TimetableRetriever`, which
 returns a list of lessons to be added.
7. The `execute` method returns a `CommandResult` object.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

![CommitActivityDiagram](images/CommitActivityDiagram.png)

#### Design consideration:

##### Aspect: How undo & redo executes

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:
* NUS Computing Students with poor time management skills who have difficulties managing their weekly academic schedule
* prefer desktop apps over other types
* can type fast and prefer typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**:
* More convenient than typical apps as lessons and assignments are managed in just one app so there is no need to switch between different ones.
* Faster than typical mouse/GUI driven apps as most features are accomplished by typing simple commands.
* Easier to manage schedule than typical scheduling apps as assignments are automatically scheduled.

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                     | So that I can…​                                                        |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | student                                    | import my timetable         | better schedule my assignments with my lesson timings taken into account              |
| `* * *`  | forgetful student                                       | receive reminders for my lessons and assignments               |  avoid forgetting to attend lessons or do my work                                                                      |
| `* * *`  | poor time manager                                       | add and schedule assignments                |  keep track of what needs to be done                                 |
| `* * *`  | poor time manager                                       | delete assignments          | remove assignments that i have completed or added wrongly |
| `* * *`    | student                                       | view lessons and assignments together   | view all assignments i have to complete amidst my lessons                |
| `* * *`      | particular student | use a scheduler with a user-friendly interface           | use the application easily and enjoyably                                                 |
| `* * *`      | new user | navigate the UI easily           | use the application efficiently                                                 |                                      |
| `* * *`      | slow/confused student | i can access and view my academic duties easily           | quickly find out what i need to do for the week
| `* * *`      | experienced vim-user | use my keyboard to key in assignments           | save time tracking down my assignments                                                 ||
| `* * `      | beginner user | view a tutorial           | benefit from the features of ProductiveNUS                                                 ||
| `* * `      | experienced vim-user | use shortcuts in my commands            | access my academic schedule more quickly                                              ||
*{More to be added}*

### Use cases

(For all use cases below, the **System** is the `Academic Schedule Manager` and the **Actor** is the `user`, unless specified otherwise)

**Use case: UC01 - Add an assignment**

**MSS**

1.  User requests to add an assignment.
2.  Academic Schedule Manager adds the assignment.
3.  Academic Schedule Manager shows a success message with details of the assignment added.

    Use case ends.

**Extensions**

* 1a. The user did not supply all required parameters.

    * 1a1. Academic Schedule Manager shows an error message.

      Use case ends.


* 1b. The given DEADLINE_OF_ASSIGNMENT parameter is in the wrong format.

    * 1b1. Academic Schedule Manager shows an error message.

      Use case ends.


**Use case: UC02 - Delete an assignment**

**MSS**

1.  User <ins>requests to list assignments and lessons (UC05)</ins>.
2.  User requests to delete a specific assignment in the list.
3.  Academic Schedule Manager shows a success message with details of the assignment deleted.

    Use case ends.

**Extensions**

* 2a. The given index is invalid (index is referring to a lesson or index is out of range).

    * 2a1. Academic Schedule Manager shows an error message.

      Use case resumes at step 2.


**Use case: UC03 - Import timetable**

**MSS**

1.  User retrieves NUSMods timetable URL from the NUSMods website.
2.  User requests to import NUSMods timetable using their NUSMods timetable URL.
3.  Academic Schedule Manager adds all the lessons according to the data retrieved.
4.  Academic Schedule Manager shows a success message with details of the lessons added.

    Use case ends.

**Extensions**

* 2a. The given URL is invalid (not a valid NUSMods timetable URL).

    * 2a1. Academic Schedule Manager shows an error message.

      Use case resumes at step 2.

* 2b. User already has a timetable imported before.

    * 2b1. Academic Schedule Manager informs user that previously imported lesson will be deleted.
    
      Use case resumes at step 3.


**Use case: UC04 - Remind**

**MSS**

1.  User <ins>requests to list assignments and lessons (UC05)</ins>.
2.  User requests to set reminder for a specific assignment in the list.
3.  Academic Schedule Manager adds the assignment to the remind list.
4.  Academic Schedule Manager shows a success message with details of the assignment set as reminder.

    Use case ends.

**Extensions**

* 2a. The given index is invalid (index is referring to a lesson or index is out of range).

    * 2a1. Academic Schedule Manager shows an error message.

      Use case resumes at step 2.


**Use case: UC05 - List assignments and lessons**

**MSS**

1.  User requests to list assignments and lessons.
2.  Academic Schedule Manager shows a list of all assignments and lessons.

    Use case ends.

**Extensions**

* 1a. User requests to list XX day(s) of assignments and lessons.

    * 1a1. Academic Schedule Manager shows a filtered list of assignments and lessons.

        Use case ends.
      
* 2a. The list is empty.

        Use case ends.
    
### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to `1000 lessons and assignments` without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The system should respond within two seconds.
5.  The system should save a user's data `after every user command`.
6.  The system should be usable by a novice who has never used the app before.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **User**: NUS Computing student

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
