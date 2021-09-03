package net.auoeke.extensions

import java.nio.file.FileVisitResult
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class FileWalker(private val action: (Path) -> Unit) : SimpleFileVisitor<Path>() {
    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        this.action(file)

        return FileVisitResult.CONTINUE
    }
}
