// 댓글을 비동기방식으로 작성해서 서버에 반영 후 가장 상단에 댓글 뿌리기
function datinsert(){

    // 댓글 널 처리
    let content = $("#insertdat").val().trim();
    if(content==""){
        alert("댓글란이 비어있습니다.");
        return;
    }

    // 현재 로그인 상태인지 확인하고 로그인페이지로 보내기
    let username = $("#username").val().trim();
    if(username==""){
        alert("로그인이 필요한 기능입니다. ㅎㅎ")
        location.href = "/user/login";
    }

    // 댓글 작성을 위한 해당 게시물의 고유 id 가져오기
    let id = $("#id").val();

    // 댓글 서버에 반영하고 댓글 목록 가장 상단에 추가 하기
    $.ajax({
        type: 'POST',
        url: '/dat/insert',
        contentType: "application/json",
        data: JSON.stringify({boardId: id, content: content}),
        success: function (response) {
            let content = response.content;
            let datid = response.id;
            let maxid = response.maxId;
            let temp_html = addcontent(content,datid,maxid);
            $("#insertdat").val("");
            $("#insertdat").focus();
            $('#datcontainer').prepend(temp_html); // 댓글 가장 상단에 추가
        }
    });
}

// 작성 한 댓글 가장 상단에 뿌려주는 폼 만들기
function addcontent(content,datid,maxid){
    let temp_html = `<div id="coordinate${maxid}"><textarea class="form-control" readonly id="datcontent${maxid}" >${content}</textarea>                        
                              <input type="hidden" id="hiddendat${maxid}">
                              <input type="hidden" id="hiddendatId${maxid}" value="${datid}">
                              <div>
                                  <button class="btn btn-outline-secondary" type="button" id="datupd${maxid}" onclick="datupd(${maxid})" style="float:right">수정</button>
                              </div>
                              <div>
                                  <button class="btn btn-outline-secondary" type="button" id="datupdc${maxid}" onclick="datupdc(${maxid})"  style="display: none; float:right">수정완료</button>
                              </div>
                              <div>
                                  <button class="btn btn-outline-secondary" type="button" id="datdel${maxid}" onclick="datdel(${maxid})" style="float:right;">삭제</button>
                              </div>
                              <div>
                              <button class="btn btn-outline-secondary" type="button" id="datcancle${maxid}" onclick="cancle(${maxid})"  style="display: none;float:right;">취소</button>
                              </div></div>`
    return temp_html;
}

// 수정완료,취소버튼으로 토글해주면서 댓글의 원래 값을 저장해둠
function datupd(n){

    // 댓글의 원래 값 저장
    let content = $("#datcontent"+n).val().trim();
    $("#hiddendat"+n).val(content);

    // 버튼 및 택스트 전환 관련
    $("#datcontent"+n).prop('readonly',false);
    $("#datupd"+n).css("display", "none");
    $("#datdel"+n).css("display", "none");
    $("#datupdc"+n).css("display", "block");
    $("#datcancle"+n).css("display", "block");
}

// 수정완료 버튼 클릭시 ajax로 수정완료 후 DB에 반영된 값 가져와서 해당위치에 반영
function datupdc(n){

    // 댓글의 원래 값 가져오기 - 수정 버튼 클릭시 datupd 함수에서 원래값을 숨겨놓음
    let original = $("#hiddendat"+n).val().trim();

    // 수정완료된 값 가져오기
    let content = $("#datcontent"+n).val().trim();

    // 원래 댓글 값이랑 같거나 비어있으면 반환처리
    if(original==content){
        alert("댓글에 변화가 없습니다. RDS요금 많이 나오면 책임져 주실 수 있으신가요? 그러면 연락주세요 010-1313-1341");
        return;
    }else if(content==""){
        alert("댓글이 비어있습니다. 채워서 바꿔주세요 ㅎㅎㅎㅎㅎ 이런시도 좋지 않아요 서버비 많이 나와요 ㅠㅠ");
        return;
    }

    // 댓글 고유id 값 가져오기
    let id = $("#hiddendatId"+n).val().trim();

    $.ajax({
        type: 'POST',
        url: '/dat/update',
        contentType: "application/json",
        data: JSON.stringify({id: id, content: content}),
        success: function (response) {
            alert("수정 완료되었습니다.");
        }
    });

    // 버튼 및 택스트 전환 관련
    $("#datcontent"+n).prop('readonly',true);
    $("#datupd"+n).css("display", "block");
    $("#datdel"+n).css("display", "block");
    $("#datupdc"+n).css("display", "none");
    $("#datcancle"+n).css("display", "none");
}

// 수정 취소시 값 다시 되돌려 놓고 버튼 토글전환
function cancle(n){
    // 댓글의 원래 값을 다시 되돌려 줌
    let content = $("#hiddendat"+n).val().trim();
    $("#datcontent"+n).val(content);
    // 버튼 및 택스트 전환 관련
    $("#datcontent"+n).prop('readonly',true);
    $("#datupd"+n).css("display", "block");
    $("#datdel"+n).css("display", "block");
    $("#datupdc"+n).css("display", "none");
    $("#datcancle"+n).css("display", "none");
}

// 댓글 삭제
function datdel(n){
    // 삭제 확인
    if (confirm("정말 삭제하시겠습니까?") == true){    //확인

        // 삭제할 댓글 고유 id
        let id = $("#hiddendatId"+n).val().trim();
        // 댓글 고유 위치 id
        let coordinate = "coordinate" + n;
        // ajax 서버에 업데이트 반영 및 반영된 값 가져와서 뿌리기
        $.ajax({
            type: 'POST',
            url: '/dat/delete',
            contentType: "application/json",
            data: JSON.stringify({id:id,coordinate:coordinate}),
            success: function (response) {
                let coordinate = response.coordinate
                $("#"+coordinate).remove(); // 삭제된 댓글 위치 확인해서 비동기식으로 바로 삭제
                alert("삭제 완료되었습니다.");
            }
        });
    }else{   //취소
        return;
    }
}