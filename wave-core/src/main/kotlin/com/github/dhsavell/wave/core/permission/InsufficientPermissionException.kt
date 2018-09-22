package com.github.dhsavell.wave.core.permission

class InsufficientPermissionException(requiredPermission: Permission) :
    Exception("You don't have the necessary permissions to do that (required \"$requiredPermission\")!")