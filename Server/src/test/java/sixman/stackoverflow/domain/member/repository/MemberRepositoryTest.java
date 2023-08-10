package sixman.stackoverflow.domain.member.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sixman.stackoverflow.domain.member.entity.Member;
import sixman.stackoverflow.global.testhelper.RepositoryTest;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends RepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("email 을 받아 Member 객체를 반환한다.")
    void findByEmail() {

        //given
        Member member = createMember();
        em.persist(member);

        //when
        Member findMember = memberRepository.findByEmail(member.getEmail()).orElseThrow();

        //then
        assertThat(findMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
    }
}