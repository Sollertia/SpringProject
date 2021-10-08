function writen() {

    let title = $('#title').val();
    let content = $('#content').text();

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
        url: '/board/writen',
        contentType: "application/json",
        data: JSON.stringify({title: title, content: content}),
        success: function (response) {
            alert(response)
            window.location.href = "/"

        }
    });

}