import java.util.*;

enum UserType {
    MANAGER,
    EMPLOYEE
}

enum TaskStatus {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    PENDING
}

class Task {
    private int taskId;
    private String taskName;
    private String taskDescription;
    private User assigner;
    private User assignedTo;
    private TaskStatus taskStatus;

    public Task(int taskId, String taskName, String taskDescription, User assigner, User assignedTo) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.assigner = assigner;
        this.assignedTo = assignedTo;
        this.taskStatus = TaskStatus.NOT_STARTED;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public User getAssigner() {
        return assigner;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "Task ID: " + taskId + ", Name: " + taskName + ", Status: " + taskStatus + 
               ", Assigned to: " + assignedTo.getUserName() + ", Created by: " + assigner.getUserName();
    }
}

class User {
    private int userId;
    private String userName;
    private UserType userType;
    private List<Task> assignedTasks;

    public User(int userId, String userName, UserType userType) {
        this.userId = userId;
        this.userName = userName;
        this.userType = userType;
        this.assignedTasks = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public UserType getUserType() {
        return userType;
    }

    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public void addTask(Task task) {
        if (!assignedTasks.contains(task)) {
            assignedTasks.add(task);
        }
    }

    public void changeTaskStatus(Task task, TaskStatus newStatus) {
        if (assignedTasks.contains(task)) {
            task.setTaskStatus(newStatus);
            System.out.println("The task '" + task.getTaskName() + "' status has been changed to " 
                + newStatus + " by " + getUserName());
        } else {
            System.out.println("Error: Task '" + task.getTaskName() + "' is not assigned to " + getUserName());
        }
    }
}

class TaskController {
    private Map<Integer, List<Task>> taskCreatedByManager = new HashMap<>();
    private Map<Integer, User> userIdMapping = new HashMap<>();

    public  synchronized void createTask(int taskId, String taskName, String taskDescription, User assigner, User assignedTo) {
        if (assigner.getUserType() == UserType.MANAGER) {
            Task task = new Task(taskId, taskName, taskDescription, assigner, assignedTo);
            assignedTo.addTask(task);
            taskCreatedByManager.computeIfAbsent(assigner.getUserId(), k -> new ArrayList<>()).add(task);
        } else {
            System.out.println("Only a Manager can create tasks!");
        }
    }

    public List<Task> getTasksByUser(int userId) {
        User user = getUserById(userId);
        return (user != null) ? user.getAssignedTasks() : new ArrayList<>();
    }

    public List<Task> getTasksByManager(int managerId) {
        return taskCreatedByManager.getOrDefault(managerId, new ArrayList<>());
    }

    public User getUserById(int userId) {
        return userIdMapping.get(userId);
    }

    public void addUser(User user) {
        userIdMapping.put(user.getUserId(), user);
    }
}

class Main {
    public static void main(String[] args) {
        TaskController taskController = new TaskController();


        User manager = new User(1, "John (Manager)", UserType.MANAGER);
        User employee1 = new User(2, "Alice", UserType.EMPLOYEE);
        User employee2 = new User(3, "Bob", UserType.EMPLOYEE);

        taskController.addUser(manager);
        taskController.addUser(employee1);
        taskController.addUser(employee2);

        taskController.createTask(101, "Finish Report", "Complete the Q1 report", manager, employee1);
        taskController.createTask(102, "Update Website", "Revamp the landing page", manager, employee2);

        List<Task> aliceTasks = taskController.getTasksByUser(employee1.getUserId());
        if (!aliceTasks.isEmpty()) {
            employee1.changeTaskStatus(aliceTasks.get(0), TaskStatus.IN_PROGRESS);
        }

        List<Task> bobTasks = taskController.getTasksByUser(employee2.getUserId());
        if (!bobTasks.isEmpty()) {
            employee2.changeTaskStatus(bobTasks.get(0), TaskStatus.COMPLETED);
        }


        System.out.println("\nTasks assigned by Manager:");
        for (Task task : taskController.getTasksByManager(manager.getUserId())) {
            System.out.println(task);
        }


        System.out.println("\nTasks assigned to Alice:");
        for (Task task : aliceTasks) {
            System.out.println(task);
        }

        System.out.println("\nTasks assigned to Bob:");
        for (Task task : bobTasks) {
            System.out.println(task);
        }
    }
}

// Use ConcurrentHashMap for taskCreatedByManager and userIdMapping


// Use Collections.synchronizedList() for assignedTasks



// Synchronize critical operations (synchronized methods)

// Protects task creation, assignment, and status updates.


