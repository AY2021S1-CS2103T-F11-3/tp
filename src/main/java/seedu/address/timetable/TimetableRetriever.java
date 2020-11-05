package seedu.address.timetable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import seedu.address.model.lesson.Lesson;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.ModuleCode;
import seedu.address.model.task.Name;

public class TimetableRetriever {
    private static final String JSON_API = "https://api.nusmods.com/v2/2020-2021/modules/";
    private static final String DOT_JSON = ".json";

    private static final LocalDate SEMESTER_1_START_DATE = LocalDate.of(2020, 8, 10);
    private static final LocalDate SEMESTER_2_START_DATE = LocalDate.of(2021, 1, 11);
    private static final LocalDate SPECIAL_1_START_DATE = LocalDate.of(2021, 5, 10);
    private static final LocalDate SPECIAL_2_START_DATE = LocalDate.of(2021, 6, 21);
    private static final LocalDate[] startDateArray = { SEMESTER_1_START_DATE, SEMESTER_2_START_DATE,
                                                        SPECIAL_1_START_DATE, SPECIAL_2_START_DATE };
    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Retrieves timetable json information from NUSMods API and prints out the relevant information.
     */
    public static List<Lesson> retrieveLessons(TimetableData timetableData) throws IOException, ParseException {
        int semester = timetableData.getSemester();
        LocalDate startDate = startDateArray[semester - 1];
        String[] moduleCodeArray = timetableData.getModuleCodeArray();
        String[] moduleLessonArray = timetableData.getModuleLessonArray();

        List<Lesson> lessons = new ArrayList<>();

        for (int moduleIter = 0; moduleIter < moduleCodeArray.length; moduleIter++) {
            String module = moduleCodeArray[moduleIter];
            URL url = getModuleUrl(module);
            String inline = httpGetModuleJsonData(url);

            JSONArray semesterSpecificTimetableData = getSemesterSpecificTimetableData(semester, inline);
            String[] specificModuleLessonArray = getSpecificModuleLessonArray(moduleLessonArray, moduleIter);

            findLessonAndAdd(lessons, module, semesterSpecificTimetableData, specificModuleLessonArray, startDate);
        }
        return lessons;
    }

    private static String getUrlString(String moduleCode) {
        return JSON_API + moduleCode + DOT_JSON;
    }

    private static URL getModuleUrl(String moduleCode) throws MalformedURLException {
        String urlString = getUrlString(moduleCode);
        return new URL(urlString);
    }

    private static String httpGetModuleJsonData(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        }

        String inline = "";
        Scanner sc = new Scanner(url.openStream());
        while (sc.hasNext()) {
            inline += sc.nextLine();
        }
        sc.close();
        return inline;
    }

    private static JSONArray getSemesterSpecificTimetableData(int sem, String inline) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject moduleData = (JSONObject) jsonParser.parse(inline);
        JSONArray semesterData = (JSONArray) moduleData.get("semesterData");
        for (int iter = 0; iter < semesterData.size(); iter++) {
            JSONObject temp = (JSONObject) semesterData.get(iter);
            if ((long) temp.get("semester") == (long) sem) {
                JSONObject bothSemesterTimetableData = (JSONObject) semesterData.get(iter);
                JSONArray semesterSpecificTimetableData = (JSONArray) bothSemesterTimetableData.get("timetable");
                return semesterSpecificTimetableData;
            }
        }
        throw new ParseException(0);
    }

    private static String[] getSpecificModuleLessonArray(String[] moduleLessonArray, int index) {
        String specificModuleLesson = moduleLessonArray[index];
        String[] specificModuleLessonArray = specificModuleLesson.split(",");
        return specificModuleLessonArray; // ["T:1","L:2"]
    }

    private static void findLessonAndAdd(List<Lesson> lessons, String module, JSONArray timetableData,
                                         String[] moduleLessonArray, LocalDate startDate) {
        for (int lessonIter = 0; lessonIter < moduleLessonArray.length; lessonIter++) {
            if (!moduleLessonArray[0].equals("")) {
                String lessonInfo = moduleLessonArray[lessonIter];
                addLesson(lessons, module, lessonInfo, timetableData, startDate);
            }
        }
    }

    private static void addLesson(List<Lesson> lessons, String module, String lessonInfo, JSONArray timetableData,
                                  LocalDate startDate) {
        String lessonType = getLessonType(lessonInfo);
        String lessonNum = getLessonNum(lessonInfo);

        for (int dataIter = 0; dataIter < timetableData.size(); dataIter++) {
            JSONObject currentData = (JSONObject) timetableData.get(dataIter);
            String currentLessonType = (String) currentData.get("lessonType");
            String currentLessonNum = (String) currentData.get("classNo");

            if (currentLessonType.equals(lessonType) && currentLessonNum.equals(lessonNum)) {
                addSpecificLesson(lessons, module, startDate, currentLessonType, currentData);
            }
        }
    }

    private static String getLessonType(String lessonInfo) {
        String lessonType = lessonInfo.split(":")[0];
        if (lessonType.equals("LEC")) {
            lessonType = "Lecture";
        }
        if (lessonType.equals("TUT")) {
            lessonType = "Tutorial";
        }
        if (lessonType.equals("LAB")) {
            lessonType = "Laboratory";
        }
        if (lessonType.equals("REC")) {
            lessonType = "Recitation";
        }
        if (lessonType.equals("SEC")) {
            lessonType = "Sectional Teaching";
        }
        return lessonType;
    }

    private static String getLessonNum(String lessonInfo) {
        String lessonNum = lessonInfo.split(":")[1];
        return lessonNum;
    }

    private static int getDayOffset(String day) {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        int dayOffset = 0;
        for (int i = 0; i < 7; i++) {
            String currDay = daysOfWeek[i];
            if (currDay.equals(day)) {
                dayOffset = i;
                break;
            }
        }
        return dayOffset;
    }

    private static int[] getWeeksAsIntegerArray(JSONArray jsonWeeks) {
        int[] weeks = new int[jsonWeeks.size()];
        for (int i = 0; i < jsonWeeks.size(); i++) {
            Long week = (Long) jsonWeeks.get(i);
            weeks[i] = week.intValue() > 6 ? week.intValue() + 1 : week.intValue();
        }
        return weeks;
    }

    private static void addSpecificLesson(List<Lesson> lessons, String module, LocalDate startDate,
                                          String currentLessonType, JSONObject currentData) {
        String day = (String) currentData.get("day");
        String name = module + " " + currentLessonType + " " + day;

        JSONArray jsonWeeks = (JSONArray) currentData.get("weeks");
        int[] weeks = getWeeksAsIntegerArray(jsonWeeks);
        int dayOffset = getDayOffset(day);

        for (int week : weeks) {
            int daysToAdd = 7 * (week - 1) + dayOffset;
            LocalDate date = startDate.plusDays(daysToAdd);
            if (date.isAfter(LocalDate.now())) {
                String startTime = date.format(LOCAL_DATE_FORMATTER) + " " + (String) currentData.get("startTime");
                String endTime = date.format(LOCAL_DATE_FORMATTER) + " " + (String) currentData.get("endTime");
                lessons.add(new Lesson(new Name(name), new Deadline(startTime), new Deadline(endTime),
                        new ModuleCode(module)));
            }
        }

    }

}
