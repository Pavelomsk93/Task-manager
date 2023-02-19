# Трекер задач

**Данная программа может**

Программа хранит в себе задачи/подзадачи/эпики.
Каждая задача имеет:
1. Название;
2. Краткое описание;
3. Уникальный идентификационный номер;
4. Статус (New, In_progress, Done);
5. Подзадачи имеют индекс эпика, которому они принадлежат;
6. Продолжительность выполнения;
7. Ориентировочное время начала выполнения;

Программа имеет следующие функции:
1. Создать задачу/подзадачу/эпик;
2. Обновить задачу/подзадачу/эпик;
3. Получить список всех задач/подзадач/эпиков;
4. Получить задачу/подзадачу/эпик по идентификатору;
5. Удалить все задачи/подзадачи/эпики;
6. Удалить задачу/подзадачу/эпик по идентификатору;
7. Получить список всех подзадач эпика;
8. Посмотреть историю просмотра задач;
9. Запись (чтение) задач/подзадач/эпиков в (из) файл(а);
10. Сортировка списка задач по времени;

Для хранения истории просмотров задач реализованы методы, позволяющие реализовать алгоритмическую сложность О(1).
Это достигается реализацией двусвязанного списка и HashMap.

Доступ к методам осуществляется с помощью HTTP-запросов и хранит свое состояние на отдельном сервере.

Программа написана на Java. Пример кода:

```java

package tracker.controllers;


import tracker.modelParametrs.StatusTask;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.exceptions.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FileBackedTasksManager extends InMemoryTaskManager  {

    private final File file;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy;HH:mm");

    public FileBackedTasksManager(File file) {
        this.file = file;
    }
    public FileBackedTasksManager(){this.file = null;}


    @Override
    public int createTask(Task task) {
        int id =super.createTask(task);
        save();
        return id;
    }
```
