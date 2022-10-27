package com.example.loginlivesession2.repository;

import com.example.loginlivesession2.entity.Folder;
import com.example.loginlivesession2.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 기존 폴더레포지토리에 커스텀 폴더레포지토리를 상속시켜준다
public interface FolderRepository extends JpaRepository<Folder, Long>, FolderRepositoryCustom {

    List<Folder> findAllByMemberOrderByDateDesc(Member member);

    List<Folder> findByMember(Member member);

}
