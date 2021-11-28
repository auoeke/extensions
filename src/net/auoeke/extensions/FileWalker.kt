package net.auoeke.extensions

import java.nio.file.*
import java.nio.file.attribute.*

class FileWalker(private val action: (Path) -> Unit) : SimpleFileVisitor<Path>() {
    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = super.visitFile(file, attrs).also {
        this.action(file)
    }
}
