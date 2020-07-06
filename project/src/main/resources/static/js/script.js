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