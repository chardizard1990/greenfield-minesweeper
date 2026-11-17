# Ticket 10: JavaFX Project Setup & Dependencies

## Summary
Configure Maven project to support JavaFX framework and prepare for GUI development.

## Details
- Add JavaFX dependency to pom.xml (version 21.0.1 or latest LTS)
- Add JavaFX Maven plugin for packaging
- Configure module system for JavaFX
- Create GuiApplication class (extends javafx.application.Application)
- Verify JavaFX can be compiled and packaged
- Document setup process in BUILD_GUIDE.md

### JavaFX Dependencies to Add
- javafx-controls
- javafx-fxml
- javafx-graphics

### JavaFX Maven Plugin Configuration
- Configure maven-assembly-plugin for packaged JAR
- Configure maven-javafx-maven-plugin for custom runtime
- Ensure fat JAR can be created

### Testing
- Verify project compiles with JavaFX dependencies
- Verify no dependency conflicts with existing code
- Verify Maven build succeeds

## Definition of Done
- [ ] JavaFX dependencies added to pom.xml
- [ ] JavaFX Maven plugins configured
- [ ] Module system configured correctly
- [ ] Application can compile with `mvn clean compile`
- [ ] No dependency conflicts with existing code
- [ ] BUILD_GUIDE.md updated with JavaFX instructions
- [ ] All 198 MVP tests still pass

## Story Points
- 3

## Notes
- JavaFX 21.0.1 is latest LTS (aligns with Java 21 LTS)
- Use maven-assembly-plugin for creating fat JAR
- Keep all game logic code untouched (100% reuse strategy)
- Verify cross-platform compatibility (Windows, Mac, Linux paths)

## Acceptance Criteria
1. Maven build produces successful compile with JavaFX classes available
2. No breaking changes to existing code
3. All 198 existing tests pass
4. JavaFX dependency versions clearly documented
