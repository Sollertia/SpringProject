
function update() {
let title = $('#title').val();
let content = $('#content').text();
let id = $('#id').val();

    if(title == ""){
        alert("제목을 입력하세요");
        $('#title').focus();
        return;
    }else if(content == ""){
        alert("내용을 입력하세요");
        $('#content').focus();
        return;
    }

$.ajax({
    type: 'POST',
    url: '/board/updateComplete',
    contentType: "application/json",
    data:  JSON.stringify({title:title,content:content,id:id}),
    success: function (response) {
        alert(response);
        if(response == "수정완료"){
            window.location.href = "/"
        }

    }
});

}