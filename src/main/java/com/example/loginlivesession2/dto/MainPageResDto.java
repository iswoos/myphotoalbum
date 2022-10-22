package com.example.loginlivesession2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPageResDto {

    private List<FolderSearchResDto> folders;
    private List<String> tags;
    private List<HashMap<String, Integer>> mytags;

}
