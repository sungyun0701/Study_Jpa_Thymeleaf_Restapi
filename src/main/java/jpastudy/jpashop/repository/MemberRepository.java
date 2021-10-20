package jpastudy.jpashop.repository;

import jpastudy.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
// final로 만들어진 생성자를 자동으로 만들어 주는 어노테이션
public class MemberRepository {
//    @PersistenceContext
    // @Autowired 대체 가능
    private final EntityManager em;

    //등록
    public void save(Member member){
        em.persist(member);
    }
    // id로 Member 하나 조회
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    //Member 모두 조회
    public List<Member> findAll() {
        //TypedQuery
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
    //name으로 Member 하나 또는 여러개 조회
    public  List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)
                .getResultList();
    }
}
