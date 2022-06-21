package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repository = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        String memberId = "memberV100";

        // save
        Member member = new Member(memberId, 10000);
        repository.save(member);

        // findById
        Member findMember = repository.findById(memberId);
        log.info("findMember : {} ", findMember);

        assertThat(findMember).isEqualTo(member);

        // update
        repository.update(memberId, 20000);
        Member updatedMember = repository.findById(memberId);
        assertThat(updatedMember.getMoney()).isEqualTo(20000);


        // delete
        repository.delete(memberId);
        assertThatThrownBy(() -> repository.findById(memberId)).isInstanceOf(NoSuchElementException.class);
    }
}