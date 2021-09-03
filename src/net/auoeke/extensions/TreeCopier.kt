package net.auoeke.extensions

import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes

open class TreeCopier(private val fromRoot: Path, private val toRoot: Path) : SimpleFileVisitor<Path>() {
    override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
        Files.createDirectories(newPath(dir))

        return FileVisitResult.CONTINUE
    }

    override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
        file.copy(newPath(file), StandardCopyOption.REPLACE_EXISTING)

        return FileVisitResult.CONTINUE
    }

    protected fun newPath(path: Path): Path = toRoot.resolve(fromRoot.relativize(path))
}
