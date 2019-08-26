### Функциональные требования

### Сущности

#### Пользователь
- есть группы карточек, которые ему принадлежат, он может их редактировать?
- есть какой-то внешний идентификатор, возможно e-mail или oAuth токен
- может создавать новые карточки
- имеет прогресс по пакам, как своим, так и чужим
- может покупать паки у других и выставлять свои на продажу

#### Группа карточек (пак)
- У группы карточек есть создатель, 
- есть цена
- есть тематика (тема?)
- есть теги (кастомные)
- есть прогресс выполнения (для каждого пользователя)
- она объединяет несколько карточек
- есть признак - продается или приватная

#### Карточка
- есть название (вопрос)
- есть контент (может быть разного типа)
- есть варианты ответов (больше одного), из них один правильный