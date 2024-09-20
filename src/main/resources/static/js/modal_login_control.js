
function toggleFileUpload(id) {
    var selectBox = document.getElementById("chose_" + id);
    var fileUploadDiv = document.getElementById("fileUpload_" + id);
    var form = document.getElementById("chose" + id);  // Lấy form theo id
    if (selectBox.value === "new") {
        fileUploadDiv.style.display = "block";
        form.enctype = "multipart/form-data";
        form.action="/apply/applyWithNewCv";
    } else {
        fileUploadDiv.style.display = "none";
        form.enctype = "application/x-www-form-urlencoded";
        form.action="/apply/applyWithOldCv";
    }
}

