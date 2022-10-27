package com.example.loginlivesession2.repository;

import com.example.loginlivesession2.entity.Folder;
import com.example.loginlivesession2.entity.Member;

import java.util.List;


// 커스텀 레포지토리에 메소드 쿼리문을 작성한다.
public interface FolderRepositoryCustom {
    List<Folder> findByKeyword(String keyword, Member member);
}
