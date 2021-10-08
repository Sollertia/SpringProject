function deleteB() {

    let id = $('#id').val();

    $.ajax({
        type: 'POST',
        url: '/board/delete',
        contentType: "application/json",
        data: JSON.stringify({id: id}),
        success: function (response) {
            alert(response);
            if(response == "삭제완료"){
                window.location.href = "/"
            }


        }
    });

}