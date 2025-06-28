$(document).ready(function() {
    // Get current page URL path
    var currentPath = window.location.pathname;
    
    // Remove all existing 'on' classes first
    $('nav dt').removeClass('on');
    $('nav a').removeClass('on');
    
    // Find matching menu item and add 'on' class
    $('nav a').each(function() {
        var href = $(this).attr('href');
        if (href && currentPath === href) {
            // Add 'on' class to the matching link
            $(this).addClass('on');
            
            // Add 'on' class to the parent dt (main menu)
            $(this).closest('dd').prev('dt').addClass('on');
            
            return false; // Break out of the loop once found
        }
    });
    
    // If no exact match found, try to match by path segments
    if ($('nav a.on').length === 0) {
        var pathSegments = currentPath.split('/').filter(function(segment) {
            return segment.length > 0;
        });
        
        if (pathSegments.length > 0) {
            var firstSegment = '/' + pathSegments[0];
            
            $('nav a').each(function() {
                var href = $(this).attr('href');
                if (href && href.startsWith(firstSegment)) {
                    $(this).addClass('on');
                    $(this).closest('dd').prev('dt').addClass('on');
                    return false; // Break out of the loop once found
                }
            });
        }
    }
});