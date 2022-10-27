package com.example.loginlivesession2.service.mainpage;

import com.example.loginlivesession2.entity.*;
import com.example.loginlivesession2.repository.FolderRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.loginlivesession2.entity.QFolder.folder;
import static com.example.loginlivesession2.entity.QFolderTag.folderTag;
import static org.springframework.util.StringUtils.hasText;


// 폴더레포지토리커스텀을 해당 클래스에 implements한다. 그 후 해당 클래스를 @Repository로 주입하고 @RequriedArgsConstructor로 JPAQueryFactory를 주입한다. (사용하기 위해서)
@RequiredArgsConstructor
@Repository
public class FolderRepositoryImpl implements FolderRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    // 여기서 커스텀 레포지토리에 적힌 것을 오버라이딩한다.
    @Override
    public List<Folder> findByKeyword(String keyword, Member member) {
        QFolder folder = QFolder.folder;
        QFolderTag folderTag = QFolderTag.folderTag;

        List<FolderTag> folderTagList = queryFactory.selectFrom(folderTag) // 폴터태그 모든 것을 리스트에 담는다.
                /*
         조인단계에서 쿼리 성능 최적화하려면
           .leftJoin(folderTag.folder,folder).on(folder.member.eq(member))로 교체 후
           where절 멤버조회 제거하면 된다.

          추가로, or절을 활용하여 search메소드를 안 써도 되는 방법은 아래와 같다.
          .where(folder.folderName.contains(keyword)
                  .or(folderTag.tagName.contains(keyword))
          )
          */
                .leftJoin(folderTag.folder,folder).fetchJoin() // 폴더테그의 폴더와 폴더를 leftjoin으로 연걸한다.
                // fetchJoin을 활용해서 N+1문제를 해결할 수 있다. 폴더태그와 폴더를 함께 조회해서 지연로딩을 X로 만든다.
                .where(folder.member.eq(member)) //본인 쓴 폴더만 조회 (Q폴더가 매개변수 멤버와 동일한지 조건문)
                .where(search(keyword)) //파일이름 or 태그 검색 / search메소드 동작
                .orderBy(folder.date.desc()) // 날짜 내림차순 정렬
                .fetch(); // 기존에 조인된 것을 다시 해제

        return folderTagList.stream()
                .map(FolderTag::getFolder)
                .distinct() // 중복제거
                .collect(Collectors.toList());
    }

    private BooleanBuilder search(String keyword){
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(keyword)){
            booleanBuilder.or(folder.folderName.contains(keyword));
        }
        if(hasText(keyword)){
            booleanBuilder.or(folderTag.tagName.contains(keyword));
        }
        return booleanBuilder;
    }
}
