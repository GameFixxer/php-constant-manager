# phpstorm-constant-manager-plugin

![Build](https://github.com/GameFixxer/phpstorm-constant-manager-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/MARKETPLACE_ID.svg)](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID)

## Template ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [ ] Get familiar with the [template documentation][template].
- [ ] Adjust the [pluginGroup](./gradle.properties) and [pluginName](./gradle.properties), as well as the [id](./src/main/resources/META-INF/plugin.xml) and [sources package](./src/main/kotlin).
- [ ] Adjust the plugin description in `README` (see [Tips][docs:plugin-description])
- [ ] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html?from=IJPluginTemplate).
- [ ] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [ ] Set the `MARKETPLACE_ID` in the above README badges. You can obtain it once the plugin is published to the marketplace.
- [ ] Set the [Plugin Signing](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html?from=IJPluginTemplate) related [secrets](https://github.com/JetBrains/intellij-platform-plugin-template#environment-variables).
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html?from=IJPluginTemplate).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.

<!-- Plugin description -->
Php Constant Manager - Replace Magic Variables with Constants Effortlessly
The Php Constant Manager plugin automates the process of identifying and refactoring "magic variables" (hard-coded string and integer literals) within PHP classes. It traverses your code, extracts these literals, and generates appropriate constants, enhancing code maintainability and reducing potential errors.

Key Features:

Automated Refactoring: Automatically detect and replace hard-coded string and integer literals in your PHP classes with well-named constants.
Intelligent Naming: Generates meaningful constant names based on the literal's context, ensuring that your code remains clean and understandable.
Selective Processing: Skips test classes and already defined constants, focusing only on parts of your code that need refactoring.
Easy Integration: Integrates seamlessly into PhpStorm's workflow, triggered through a simple action within the IDE.
Usage Instructions:

Install the Php Constant Manager plugin through the JetBrains Plugin Marketplace.
Open any PHP file within PhpStorm and navigate to a class with hard-coded literals.
Trigger the action by right-clicking within the editor or through the "Replace Magic Variables" -> "automatic" action in the command palette.
The plugin will analyze the file, generate constants, and replace the literals, providing log information about the changes made.
This plugin is ideal for developers looking to enhance code quality by reducing the reliance on hard-coded values and ensuring that constants are used consistently throughout the codebase.
</description>
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "php-constant-manager"</kbd> >
  <kbd>Install</kbd>
  
- Manually:

  Download the [latest release](https://github.com/GameFixxer/phpstorm-constant-manager-plugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
