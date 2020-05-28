function toggleUserDropdownMenu() {
    var userDropdown = $('.user-dropdown');
    var dropdownArrow = $('.dropdown-arrow');
    var activeClass = ('active');

    userDropdown.hasClass(activeClass) ? userDropdown.removeClass(activeClass) :
        userDropdown.addClass(activeClass);

    dropdownArrow.hasClass(activeClass) ? dropdownArrow.removeClass(activeClass) :
        dropdownArrow.addClass(activeClass);
}