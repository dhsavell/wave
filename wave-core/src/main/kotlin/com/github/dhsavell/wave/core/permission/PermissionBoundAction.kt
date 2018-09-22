package com.github.dhsavell.wave.core.permission

/**
 * Represents an action that has a Permission associated with it.
 */
interface PermissionBoundAction {

    /**
     * String descriptor used for saving any permission overrides associated with this PermissionBoundAction.
     */
    val permissionDescriptor: String

    /**
     * Default permission used for this action.
     */
    val defaultPermission: Permission
}