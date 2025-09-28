import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class Student {
    private final String id;
    public Student(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
}

class Assignment {
    private final String details;
    private final Set<String> submittedStudents = new HashSet<>();
    public Assignment(String details) {
        this.details = details;
    }
    public String getDetails() {
        return details;
    }
    public void submit(String studentId) {
        submittedStudents.add(studentId);
    }
    public boolean isSubmitted(String studentId) {
        return submittedStudents.contains(studentId);
    }
}

class Classroom {
    private final String name;
    private final Map<String, Student> students = new HashMap<>();
    private final List<Assignment> assignments = new ArrayList<>();
    public Classroom(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void enrollStudent(Student student) {
        students.put(student.getId(), student);
    }
    public boolean hasStudent(String studentId) {
        return students.containsKey(studentId);
    }
    public Collection<Student> getStudents() {
        return students.values();
    }
    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }
    public List<Assignment> getAssignments() {
        return assignments;
    }
}

class VirtualClassroomManager {
    private static VirtualClassroomManager instance;
    private final Map<String, Classroom> classrooms = new HashMap<>();
    private final Logger logger = Logger.getLogger(VirtualClassroomManager.class.getName());
    private VirtualClassroomManager() {}
    public static synchronized VirtualClassroomManager getInstance() {
        if (instance == null) {
            instance = new VirtualClassroomManager();
        }
        return instance;
    }
    public void addClassroom(String name) {
        if (classrooms.containsKey(name)) {
            logger.warning("Classroom already exists: " + name);
            return;
        }
        classrooms.put(name, new Classroom(name));
        System.out.println("Classroom " + name + " has been created.");
    }
    public void removeClassroom(String name) {
        if (classrooms.remove(name) != null) {
            System.out.println("Classroom " + name + " has been removed.");
        } else {
            logger.warning("Attempted to remove non-existing classroom: " + name);
        }
    }

    public void listClassrooms() {
        if (classrooms.isEmpty()) {
            System.out.println("No classrooms available.");
        } else {
            classrooms.keySet().forEach(System.out::println);
        }
    }
    public void addStudent(String studentId, String className) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            logger.warning("Classroom not found: " + className);
            return;
        }
        classroom.enrollStudent(new Student(studentId));
        System.out.println("Student " + studentId + " has been enrolled in " + className + ".");
    }
    public void listStudents(String className) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            logger.warning("Classroom not found: " + className);
            return;
        }
        if (classroom.getStudents().isEmpty()) {
            System.out.println("No students in " + className);
        } else {
            classroom.getStudents().forEach(s -> System.out.println(s.getId()));
        }
    }
    public void scheduleAssignment(String className, String details) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null) {
            logger.warning("Classroom not found: " + className);
            return;
        }
        classroom.addAssignment(new Assignment(details));
        System.out.println("Assignment for " + className + " has been scheduled.");
    }
    public void submitAssignment(String studentId, String className, String details) {
        Classroom classroom = classrooms.get(className);
        if (classroom == null || !classroom.hasStudent(studentId)) {
            logger.warning("Invalid classroom or student for submission.");
            return;
        }
        Optional<Assignment> assignmentOpt = classroom.getAssignments()
                .stream().filter(a -> a.getDetails().equals(details)).findFirst();
        if (assignmentOpt.isPresent()) {
            assignmentOpt.get().submit(studentId);
            System.out.println("Assignment submitted by Student " + studentId + " in " + className + ".");
        } else {
            logger.warning("Assignment not found in class: " + className);
        }
    }
}

// Main CLI
public class VirtualClassroomApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VirtualClassroomManager manager = VirtualClassroomManager.getInstance();

        System.out.println("Welcome to the Virtual Classroom Manager.");
        System.out.println("Type 'help' to see available commands.");

        while (true) {
            try {
                System.out.print("> ");
                String input = scanner.nextLine();
                String[] tokens = input.split(" ", 2);

                switch (tokens[0]) {
                    case "exit":
                        System.out.println("Exiting Virtual Classroom Manager.");
                        return;
                    case "help":
                        System.out.println("Available commands:");
                        System.out.println("add_classroom <name>");
                        System.out.println("remove_classroom <name>");
                        System.out.println("list_classrooms");
                        System.out.println("add_student <id> <className>");
                        System.out.println("list_students <className>");
                        System.out.println("schedule_assignment <className> <details>");
                        System.out.println("submit_assignment <id> <className> <details>");
                        break;
                    case "add_classroom":
                        manager.addClassroom(tokens[1]);
                        break;
                    case "remove_classroom":
                        manager.removeClassroom(tokens[1]);
                        break;
                    case "list_classrooms":
                        manager.listClassrooms();
                        break;
                    case "add_student": {
                        String[] argsParts = tokens[1].split(" ");
                        manager.addStudent(argsParts[0], argsParts[1]);
                        break;
                    }
                    case "list_students":
                        manager.listStudents(tokens[1]);
                        break;
                    case "schedule_assignment": {
                        String[] argsParts = tokens[1].split(" ", 2);
                        manager.scheduleAssignment(argsParts[0], argsParts[1]);
                        break;
                    }
                    case "submit_assignment": {
                        String[] argsParts = tokens[1].split(" ", 3);
                        manager.submitAssignment(argsParts[0], argsParts[1], argsParts[2]);
                        break;
                    }
                    default:
                        System.out.println("Unknown command. Type 'help' for commands.");
                }
            } catch (Exception e) {
                Logger.getLogger(VirtualClassroomApp.class.getName())
                        .log(Level.SEVERE, "Error processing command", e);
                System.out.println("An error occurred. Please try again.");
            }
        }
    }
}

