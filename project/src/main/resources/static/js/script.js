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
}