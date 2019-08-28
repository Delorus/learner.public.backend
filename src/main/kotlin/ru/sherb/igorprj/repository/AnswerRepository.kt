package ru.sherb.igorprj.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.sherb.igorprj.entity.Answer

/**
 * @author maksim
 * @since 28.08.2019
 */
@Repository
interface AnswerRepository : CrudRepository<Answer, Int>