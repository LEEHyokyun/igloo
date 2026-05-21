const scriptName = "이글루봇";
/**
 * (string) room
 * (string) sender
 * (boolean) isGroupChat
 * (void) replier.reply(message)
 * (boolean) replier.reply(room, message, hideErrorToast = false) // 전송 성공시 true, 실패시 false 반환
 * (string) imageDB.getProfileBase64()
 * (string) packageName
 */

function response(room, msg, sender, isGroupChat, replier, imageDB, packageName) {

    /*
     * 출석조회
     * 스터디장 전용(김평숙) / /출석조회
    */


    // "/출석조회" 이고, 보낸 사람이 스터디장("김평숙") 인 경우만 실행
    if (msg.startsWith("/출석조회") && sender !== "김평숙") {
        return;
    }

    if (msg.startsWith("/출석조회") && sender === "김평숙") {

        try {

            const url1 = "https://igloo-62ba.onrender.com/attendance/select";

            const connection = org.jsoup.Jsoup.connect(url1)
                .ignoreContentType(true)
                .method(org.jsoup.Connection.Method.GET)
                .timeout(5000);

            const response = connection.execute();
            const json = JSON.parse(response.body());

            function makeNameText(list) {

                if (!list || list.length === 0) {
                    return "-";
                }

                return "- " + list.join(", ");
            }

            let result1 = "";

            result1 += "8시 : " + json.option1AttenderCount + "명 출석\n";
            result1 += makeNameText(json.option1AttenderList);
            result1 += "\n\n";

            result1 += "9시 : " + json.option2AttenderCount + "명 출석\n";
            result1 += makeNameText(json.option2AttenderList);
            result1 += "\n\n";

            result1 += "10시 30분 : " + json.option3AttenderCount + "명 출석\n";
            result1 += makeNameText(json.option3AttenderList);
            result1 += "\n\n";

            result1 += "불참자 : "+ json.absenceCount +"명\n";
            result1 += makeNameText(json.absenceList);
            result1 += "\n\n";

            result1 += "미응답자(지각자) : "+ json.noReponserCount +"명\n";
            result1 += makeNameText(json.noReponserList);

            replier.reply(result1);

        } catch (e) {
            replier.reply("출결 조회 중 오류가 발생하였습니다. 다시 한번 시도해주세요.\n" + e);
        }

        return;

    }



    /*
     * 출석체크
     *
    */

    // 사용 예시
    // /출석 8
    // /불참 개인사유

    //평어
    if (!msg.startsWith("/출석") && !msg.startsWith("/불참")) {
        return;
    }

    //명령어에 근접한 오타

    try {

        // 공백 기준 split
        const parts = msg.trim().split(/\s+/);

        // /출석 포함 총 4개여야 함>
        // [0] /출석 or /비출석
        // [1] 시간 또는 사유
        if (parts.length !== 2) {
            replier.reply(
                "잘못된 형태의 입력입니다. 띄어쓰기에 유의하여 입력해주세요.\n" +
                "예시) /출석 8\n" +
                "예시) /불참 개인사유"
            );

            return;

        }

        const attendanceName = sender.replace(/\s/g, "");
        const attendanceStatus = parts[0].replace("/", "");
        const thirdValue = parts[1];

        //sender 파싱 확인
        Log.info("check " + attendanceName);

        // 상태 검증
        if (attendanceStatus !== "출석" && attendanceStatus !== "불참") {
            replier.reply("출석 상태는 \"출석\" 또는 \"불참\"만 입력 가능합니다.");
            return;
        }

        let attendanceTime = 0;
        let reason = "";

        // 출석(Y) -> 시간 입력
        if (attendanceStatus === "출석") {

            // 숫자 검증
            if (!/^\d+$/.test(thirdValue)) {
                replier.reply(
                    "출석 시 올바른 출석 시간을 숫자형태로 입력해주세요.\n" +
                    "예시) /출석 8 or 930 or 10"
                );

                return;

            }

            attendanceTime = parseInt(thirdValue, 10);

            // 허용 시간: 8 / 930 / 10 만 가능
            const allowedTimes = [8, 930, 10];

            if (!allowedTimes.includes(attendanceTime)) {
                replier.reply(
                    "출석 시간은 8, 930, 10 만 입력 가능합니다.\n" +
                    "예시) /출석 8"
                );

                return;

            }

            if (attendanceTime < 0) {
                replier.reply("시간은 0 이상의 숫자만 입력 가능합니다.");

                return;

            }
        }

        // 결석(N) -> 사유 입력
        if (attendanceStatus === "불참") {

            if (
                thirdValue == null ||
                thirdValue.trim() === "" ||
                thirdValue.trim().includes("출석") ||
                thirdValue.trim().includes("참석") ||
                /^\d+$/.test(thirdValue)
            ) {

                replier.reply(
                    "불참 시 사유를 간단하게(올바르게) 입력해주세요.\n" +
                    "예시) /불참 개인사유"
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

        const url2 = "https://igloo-62ba.onrender.com/attendance/save";

        // POST 요청
        const res = org.jsoup.Jsoup
            .connect(url2)
            .ignoreContentType(true)
            .header("Content-Type", "application/json")
            .requestBody(JSON.stringify(requestBody))
            .method(org.jsoup.Connection.Method.POST)
            .execute();

        // 응답 JSON
        const result2 = JSON.parse(res.body());

        // 성공 응답
        replier.reply(
            result2.attendanceName + " 님의 " +
            " 출석 데이터 반영 완료.\n" +
            " 출석 ID : [" + result2.attendanceId + "]"
        );

    } catch (e) {

        replier.reply(
            sender + "님의 출석 데이터 처리 중 오류가 발생했습니다. 다시 한번 시도해주세요.\n" +
            e
        );
    }
}



//아래 4개의 메소드는 액티비티 화면을 수정할때 사용됩니다.
function onCreate(savedInstanceState, activity) {
    var textView = new android.widget.TextView(activity);
    textView.setText("Hello, World!");
    textView.setTextColor(android.graphics.Color.DKGRAY);
    activity.setContentView(textView);
}

function onStart(activity) {}

function onResume(activity) {}

function onPause(activity) {}

function onStop(activity) {}