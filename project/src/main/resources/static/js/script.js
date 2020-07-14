$(document).on('keydown', function (e) {
    if (e.keyCode === 27) { // ESC
        deactivateAllPopups();
    }
});

function dimBackgroundScreen() {
    $('#dimming').removeClass('deactivated');
}

function deactivateAllPopups() {
    $('.popup').removeClass('active');
    //$('.followers-list').removeClass('active');

    $('#dimming').addClass('deactivated');

    $('.confirm-user-unfollow-popup').removeClass('active');
    $('.confirm-user-log-out-popup').removeClass('active');
}

function deactivateDimming() {
    deactivateAllPopups();
    $('#dimming').addClass('deactivated');
}

function toggleUserDropdownMenu() {
    let userDropdown = $('.user-dropdown');
    let dropdownArrow = $('.dropdown-arrow');
    let activeClass = ('active');

    userDropdown.hasClass(activeClass) ? userDropdown.removeClass(activeClass) :
        userDropdown.addClass(activeClass);

    dropdownArrow.hasClass(activeClass) ? dropdownArrow.removeClass(activeClass) :
        dropdownArrow.addClass(activeClass);
}

function showUserLogoutPopup() {
    $('#dimming').removeClass('deactivated');
    $('.confirm-user-log-out-popup').addClass('active');
}

function showAddPostField() {
    dimBackgroundScreen();
    $('.add-post-field').addClass('active');
}

function toggleLikePost(postId) {
    let likeDiv = $('div[data-id=' + postId + ']');
    if (!likeDiv.hasClass("liked")) {
        $.ajax({
            type: "POST",
            url: "/post/like/" + postId + "",
            dataType: "json",
            contentType: 'application/json',
            mimeType: 'application/json',
            success: function () {
                if (!likeDiv.hasClass("liked")) {
                    likeDiv.removeClass("disliked");
                    likeDiv.addClass("liked");
                    let postLikesCounter = $('.post-likes-counter[data-id=' + postId + ']');
                    let likesCount = parseInt(postLikesCounter.text());
                    postLikesCounter.text(likesCount + 1);
                }
            },
            error: function () {
            }
        });
    } else {
        $.ajax({
            type: "POST",
            url: "/post/dislike/" + postId + "",
            dataType: "json",
            contentType: 'application/json',
            mimeType: 'application/json',
            success: function () {
                if (likeDiv.hasClass("liked")) {
                    likeDiv.removeClass("liked");
                    likeDiv.addClass("disliked");
                    let postLikesCounter = $('.post-likes-counter[data-id=' + postId + ']');
                    let likesCount = parseInt(postLikesCounter.text());
                    postLikesCounter.text(likesCount - 1);
                }
            },
            error: function () {
            }
        });
    }
}

function openCommentsPopup(postId) {
    //alert(postId);
    setPostId(postId);
    loadPostComments(postId);
    $('.comments-list').addClass('active');
    $('#dimming').removeClass('deactivated');
}

function commentHtml() {
    return '<div class="comment-section">\n' +
        '                        <p class="comment-username">${username} at ${date}</p>\n' +
        '                        <p class="comment-content">${text}</p>\n' +
        '                    </div>';
}

function buildHtmlComments(comments) {
    let div = $('#commentsIdDiv').html('');
    for (let key in comments) {
        div.html(
            div.html() + commentHtml()
                .replace("${username}", comments[key].author)
                .replace("${text}", comments[key].text)
                .replace("${date}", comments[key].date)
        );
    }
}

function loadPostComments(postId) {
    $.ajax({
        type: "GET",
        url: "/comment/get/" + postId + "",
        dataType: "json",
        success: function (data) {
            if (data.success) {
                buildHtmlComments(data.comments);
            }
        },
        error: function () {
        }
    });
}

function postComment() {
    let postId = $('#postId').val();
    $.ajax({
        type: "POST",
        url: "/comment/add",
        dataType: "json",
        data: $('#add-comment-form').serialize(),
        success: function (data) {
            if (data.success) {
                $('#add-comment-form').removeClass("comment-input-error");
                loadPostComments(postId);
                let postCommentCounter = $('.post-comments-counter[data-id=' + postId + ']');
                let commentsCount = parseInt(postCommentCounter.text());
                postCommentCounter.text(commentsCount + 1);
            } else {
                $('#add-comment-form').addClass("comment-input-error");
            }
            $('#text').val('');
        },
        error: function (data) {
        }
    });
}

function setPostId(postId) {
    $('#postId').val(postId);
}

function addPostFlag() {
    $('#isDraft').val(0);
}

function addDraftFlag() {
    $('#isDraft').val(1);
}

function closeCreatePostPopup() {
    $('.add-post-field').removeClass('active');
    deactivateAllPopups();
}