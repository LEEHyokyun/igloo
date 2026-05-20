package com.igloo.common.attendance.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum MemberNames {
    이효균("이효균"),
    강래구("강래구"),
    김민주("김민ㅈ"),
    김평숙("김평숙"),
    이다은("다은"),
    송재연("송재연"),
    심재경("심재경"),
    안성훈("안성훈"),
    유용우("유용우(요우)"),
    이전제("이전제"),
    이찬희("찬희"),
    최종민("최종민"),
    최지호("최지호"),
    최현호("최현호/coleman"),
    한찬희("한찬희"),
    홍세영("홍세영"),
    홍태의("홍태의"),
    나호준("HojunNa"),
    NO_MEMBER_EXCEPTION("")
    ;

    private final String nickName;

    public static MemberNames from(String nickName){

        for (MemberNames memberName : values()) {
            if (memberName.nickName.equals(nickName)) {
                return memberName;
            }
        }

        log.error("[ERROR][TimeSelections.from] No Member name Matched found={}", nickName);
        return MemberNames.NO_MEMBER_EXCEPTION;

    }
}
