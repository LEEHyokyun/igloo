function response(room, msg, sender, isGroupChat, replier, imageDB, packageName) {

    // 사용 예시
    // /출석 이효균 Y 8
    // /출석 이효균 N 개인사유

    if (!msg.startsWith("/출석")) {
        return;
    }

    try {

        // 공백 기준 split
        const parts = msg.trim().split(/\s+/);

        // /출석 포함 총 4개여야 함
        // [0] /출석
        // [1] 이름
        // [2] 상태(Y/N)
        // [3] 시간 또는 사유
        if (parts.length !== 4) {
            replier.reply(
                "잘못된 형태의 입력입니다.\n" +
                "예시) /출석 이효균 Y 8\n" +
                "예시) /출석 이효균 N 개인사유"
            );
            return;
        }

        const attendanceName = parts[1];
        const attendanceStatus = parts[2];
        const thirdValue = parts[3];

        // 상태 검증
        if (attendanceStatus !== "Y" && attendanceStatus !== "N") {
            replier.reply("출석 상태는 Y 또는 N 만 입력 가능합니다.");
            return;
        }

        let attendanceTime = 0;
        let reason = "";

        // 출석(Y) -> 시간 입력
        if (attendanceStatus === "Y") {

            // 숫자 검증
            if (!/^\d+$/.test(thirdValue)) {
                replier.reply(
                    "출석(Y) 시 올바른 출석 시간을 숫자형태로 입력해주세요.\n" +
                    "예시) /출석 이효균 Y 8 or 930 or 10"
                );
                return;
            }

            attendanceTime = parseInt(thirdValue, 10);

            // 허용 시간: 8 / 930 / 10 만 가능
            const allowedTimes = [8, 930, 10];

            if (!allowedTimes.includes(attendanceTime)) {
                replier.reply(
                    "출석 시간은 8, 930, 10 만 입력 가능합니다.\n" +
                    "예시) /출석 이효균 Y 8"
                );
                return;
            }

            if (attendanceTime < 0) {
                replier.reply("시간은 0 이상의 숫자만 입력 가능합니다.");
                return;
            }
        }

        // 결석(N) -> 사유 입력
        if (attendanceStatus === "N") {

            if (
                thirdValue == null ||
                thirdValue.trim() === "" ||
                /^\d+$/.test(thirdValue)
            ) {
                replier.reply(
                    "결석(N) 시 사유를 간단하게(올바르게) 입력해주세요.\n" +
                    "예시) /출석 이효균 N 개인사유"
                );
                return;
            }

            reason = thirdValue;
        }

        // 요청 객체
        const requestBody = {
            attendanceName: attendanceName,
            attendanceStatus: attendanceStatus,
            attendanceTime: attendanceTime,
            reason: reason
        };

        const url = "https://igloo-62ba.onrender.com/attendance/save";

        // POST 요청
        const response = org.jsoup.Jsoup
            .connect(url)
            .ignoreContentType(true)
            .header("Content-Type", "application/json")
            .requestBody(JSON.stringify(requestBody))
            .method(org.jsoup.Connection.Method.POST)
            .execute();

        // 응답 JSON
        const result = JSON.parse(response.body());

        // 성공 응답
        replier.reply(
            attendanceName + "님, " +
            result.attendanceId +
            " 출석 데이터 반영 완료."
        );

    } catch (e) {

        replier.reply(
            "출석 데이터 처리 중 오류가 발생했습니다. 다시 한번 시도해주세요.\n" +
            e
        );
    }
}